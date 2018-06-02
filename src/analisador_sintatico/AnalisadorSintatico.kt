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

    init {
        iniciarTabelaTransicoes()
    }

    fun analisarSintaxe() {
        while (true) {

        }
    }

    private fun iniciarTabelaTransicoes() {
        val inputStream: InputStream = File("Analisador_Sintatico_Utils\\tabela_transicoes.csv").inputStream()
        val inputString = inputStream.bufferedReader().use { it.readText() }

        var linha = 0
        val listaSimbolos = ArrayList<String>()
        inputString.reader().forEachLine {
            val input = it.split(",")

            var estado = input[0]
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

}