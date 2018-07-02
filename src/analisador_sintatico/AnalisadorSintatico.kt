package analisador_sintatico

import analisador_lexico.AnalizadorLexico
import analisador_lexico.Lexema
import analisador_semantico.AnalisadorSemantico
import analisador_semantico.ErroSemantico
import java.io.File
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AnalisadorSintatico (val analisadorLexico: AnalizadorLexico, val analisadorSemantico: AnalisadorSemantico) {

    private val pilhaDeEstados: Stack<String> = Stack()
    private val pilhaSemantica: Stack<Lexema> = Stack()
    private val tabelaTransicoes: HashMap<String, String> = HashMap()
    private val tabelaReducoes: HashMap<String, Producao> = HashMap()

    init {
        iniciarTabelaTransicoes()
        iniciarTabelaReducoes()
    }

    fun analisarSintaxe() {
        var entrada = analisadorLexico.getNextLexema()

        pilhaDeEstados.push("0")
        while (true) {
            if (!pilhaSemantica.empty() && pilhaSemantica.peek().token.equals("ERRO SMT")) {
                println(ErroSemantico(pilhaSemantica.peek().lexema))
                break
            }

            val action = action(pilhaDeEstados.peek().toInt(), entrada)
            if (action.startsWith("ERRO")) {
                if (action.endsWith("SNT"))
                    println(ErroSintatico(action.split(" ")[1], entrada, analisadorLexico.getLinhaColuna()))
                else
                    println("Erro desconhecido :'(")
                break
            }
            else if (action.equals("ACC")) {
                println("\n-----------  Build Successful! :D -----------\n")
                break
            }
            else if (action[0].equals('S')) {
                shift(action.substring(1), entrada)
                entrada = analisadorLexico.getNextLexema()
            }
            else if (action[0].equals('R')) {
                reduce(action.substring(1))
            }
        }
    }

    private fun shift(estado: String, entrada: Lexema) {
        pilhaSemantica.push(entrada)
        pilhaDeEstados.push(estado)
    }

    private fun reduce(numProducao: String) {
        val producao = tabelaReducoes[numProducao]!!

        val params: HashMap<String, Lexema> = HashMap()


        if (producao.reducao.equals("EXP_R") || producao.reducao.equals("LD")){
            var oprdCount = 2

            producao.argumentos.reversed().forEach{
                pilhaDeEstados.pop()

                val lexema : Lexema = pilhaSemantica.pop()

                if (it.equals("OPRD")) {
                    params[lexema.token + oprdCount] = lexema
                    oprdCount--
                }
                else
                    params[lexema.token] = lexema
            }
        }
        else {
            producao.argumentos.reversed().forEach {
                pilhaDeEstados.pop()

                val lexema: Lexema = pilhaSemantica.pop()

                params[lexema.token] = lexema
            }
        }

        goto(analisadorSemantico.analisarSemantica(producao.reducao, params))

        //println(producao.toString())
    }

    private fun goto(reducao: Lexema) {
        val action = action(pilhaDeEstados.peek().toInt(), reducao)

        shift(action.substring(1), reducao)
    }

    private fun action(estado: Int, entrada: Lexema) : String {
        return when {
            entrada.token.equals("ERRO SMT") -> entrada.token
            tabelaTransicoes["$estado${entrada.token}"] != null -> tabelaTransicoes["$estado${entrada.token}"]!!
            else -> "ERRO $estado SNT"
        }
    }

    private fun iniciarTabelaTransicoes() {
        val inputStream: InputStream = File("Analisador_Sintatico_Utils\\tabela_transicoes.csv").inputStream()
        val inputString = inputStream.bufferedReader().use { it.readText() }

        var linha = 0
        val listaSimbolos = ArrayList<String>()
        inputString.reader().forEachLine {
            val input = it.split(",")

            val estado = input[0]
            var coluna = -1

            input.forEach {
                if (linha == 0 && it != "") {
                    listaSimbolos.add(it)
                }
                else if (it != "" && coluna > -1) {
                    tabelaTransicoes[estado + listaSimbolos[coluna]] = it
                }

                coluna++
            }

            linha++
        }
    }

    private fun iniciarTabelaReducoes() {
        val inputStream: InputStream = File("Analisador_Sintatico_Utils\\tabela_producoes.csv").inputStream()
        val inputString = inputStream.bufferedReader().use { it.readText() }

        inputString.reader().forEachLine {
            val input = it.split(",")

            tabelaReducoes[input[0]] = Producao(input[1], input[2].split(" "))
        }
    }
}