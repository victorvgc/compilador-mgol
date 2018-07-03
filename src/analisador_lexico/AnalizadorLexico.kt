package analisador_lexico

import analisador_lexico.utils.Saida
import java.io.CharArrayReader

class AnalizadorLexico (val tabelaSimbolos: HashMap<String, Lexema>, input: String){

    //val test: String = "3 - 4 + (-1) - (-0)"
    //val charArray = test.toCharArray()

    val charArray = input.toCharArray()

    val charArrayReader: CharArrayReader = CharArrayReader(charArray)

    var linhaAtual = 1
    var colunaAtual = 1

    private fun analizarLexema(): Lexema {
        val afdLexico = AFDLexico()

        var saida: Int

        var lexema = ""

        var char: Char

        do {
            charArrayReader.mark(1)
            char = charArrayReader.read().toChar()
            saida = afdLexico.processar(char)
            colunaAtual++

            if (saida == Saida.IGNORAR)
                break
            if (saida != Saida.SEM_TRANSICAO)
                lexema += char
        }while (saida != Saida.SEM_TRANSICAO && saida != Saida.ERRO)

        if (saida != Saida.IGNORAR) {
            charArrayReader.reset()
            colunaAtual--

            char = charArrayReader.read().toChar()
            if (char.toInt() == 0xFFFF && lexema.isEmpty())
                return Lexema(14, lexema)
            else
                charArrayReader.reset()
        }
        else
            return analizarLexema()

        if (!afdLexico.isCadeiaValida())
            return ErroLexico(char.toString(), linhaAtual, colunaAtual)
        return Lexema(afdLexico.ultimoEstadoAlcancado, lexema)
    }

    private fun nextLexema(): Lexema {
        val afdLexico = AFDLexicoHashMap()

        var saida: Int

        var lexema = ""

        var char: Char

        do {
            charArrayReader.mark(1)
            char = charArrayReader.read().toChar()
            saida = afdLexico.processar(char)

            if (char.toInt() == 9)
                colunaAtual += 4
            else
                colunaAtual++

            if ((char.toInt() == 10)) {
                linhaAtual++
                colunaAtual = 1
            }

            if (saida == Saida.IGNORAR)
                break
            if (saida != Saida.SEM_TRANSICAO  && saida != Saida.ACEITO)
                lexema += char
        }while (saida == Saida.PROCESSANDO)

        if (saida != Saida.IGNORAR) {
            charArrayReader.reset()
            colunaAtual--

            char = charArrayReader.read().toChar()
            if (char.toInt() == 0xFFFF && lexema.isEmpty())
                return Lexema(14, lexema)
            else
                charArrayReader.reset()
        }
        else
            return nextLexema()

        if (saida != Saida.ACEITO)
            return ErroLexico(char.toString(), linhaAtual, colunaAtual)
        return Lexema(afdLexico.ultimoEstadoAlcancado, lexema)
    }

    private fun isAlreadyOnTabelaSimbolos (lexema: Lexema): Boolean {
        val token: Lexema? = tabelaSimbolos[lexema.lexema]

        return token != null
    }

    private fun adicionarNaTabelaSimbolos(lexema: Lexema): Lexema {

        if (!isAlreadyOnTabelaSimbolos(lexema) && lexema.token.equals("id"))
            tabelaSimbolos[lexema.lexema] = lexema

        else if (lexema.token.equals("id")) {
            return tabelaSimbolos[lexema.lexema]!!
        }

        return lexema
    }

    fun analizar(): Lexema {
        val lexema = analizarLexema()
        return adicionarNaTabelaSimbolos(lexema)
    }

    fun getNextLexema(): Lexema {
        val lexema = nextLexema()
        return adicionarNaTabelaSimbolos(lexema)
    }

    fun getLinhaColuna(): String {
        return "Linha: $linhaAtual. Coluna: $colunaAtual"
    }

    fun getLinha(): Int {
        return linhaAtual
    }
}