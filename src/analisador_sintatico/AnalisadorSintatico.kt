package analisador_sintatico

import analisador_lexico.AnalizadorLexico
import java.io.File
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AnalisadorSintatico (val analisadorLexico: AnalizadorLexico) {

    private val pilha: Stack<String> = Stack()
    private val tabelaTransicoes: HashMap<String, String> = HashMap()
    private val tabelaReducoes: HashMap<String, Producao> = HashMap()

    init {
        iniciarTabelaTransicoes()
        iniciarTabelaReducoes()
    }

    fun analisarSintaxe() {
        var entrada = analisadorLexico.getNextLexema()

        pilha.push("0")
        while (true) {
            val action = action(pilha.peek().toInt(), entrada.token)
            if (action.startsWith("ERRO")) {
                println(ErroSintatico(action.split(" ")[1], entrada, analisadorLexico.getLinhaColuna()))
                break
            }
            else if (action.equals("ACC")) {
                println("\n-----------  Accepted!  -----------\n")
                break
            }
            else if (action[0].equals('S')) {
                shift(action.substring(1), entrada.token)
                entrada = analisadorLexico.getNextLexema()
            }
            else if (action[0].equals('R')) {
                reduce(action.substring(1))
            }
        }
    }

    private fun shift(estado: String, entrada: String) {
        pilha.push(entrada)
        pilha.push(estado)
    }

    private fun reduce(numProducao: String) {
        val producao = tabelaReducoes[numProducao]!!

        producao.argumentos.reversed().forEach{
            pilha.pop()
            pilha.pop()
        }

        goto(producao.reducao)
        println(producao.toString())
    }

    private fun goto(reducao: String) {
        val action = action(pilha.peek().toInt(), reducao)

        shift(action.substring(1), reducao)
    }

    private fun action(estado: Int, entrada: String) : String {
        return if (tabelaTransicoes["$estado$entrada"] != null)
            tabelaTransicoes["$estado$entrada"]!!
        else
            "ERRO $estado"
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