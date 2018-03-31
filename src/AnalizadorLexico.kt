import utils.Saida
import java.io.CharArrayReader

class AnalizadorLexico (val tabelaSimbolos: HashMap<String, Lexema>, input: String){

    //val test: String = "3 - 4 + (-1) - (-0)"
    //val charArray = test.toCharArray()

    val charArray = input.toCharArray()

    val charArrayReader: CharArrayReader = CharArrayReader(charArray)

    var linhaAtual = 1
    var colunaAtual = 1

    fun analizarLexema(): Lexema{
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
            else if ((char.toInt() == 10 || char.toInt() == 13)) {
                linhaAtual++
                colunaAtual = 0
            }
            else if (char.toInt() == 9)
                colunaAtual += 4
            else
                charArrayReader.reset()
        }
        else
            return analizarLexema()

        if (!afdLexico.isCadeiaValida())
            return Erro(char.toString(), linhaAtual, colunaAtual)
        return Lexema(afdLexico.ultimoEstadoAlcancado, lexema)
    }

    private fun alreadyIsOnTabelaSimbolos (lexema: Lexema): Boolean {
        val token: Lexema? = tabelaSimbolos[lexema.lexema]

        return token != null
    }

    fun atualizarTabelaSimbolos(lexema: Lexema): Lexema {
        if (!alreadyIsOnTabelaSimbolos(lexema)) {
            tabelaSimbolos[lexema.lexema] = lexema
            return lexema
        }
        else {
            tabelaSimbolos[lexema.lexema]!!.update(lexema)

            return tabelaSimbolos[lexema.lexema]!!
        }
    }

    fun analizar(): Lexema {
        val lexema = analizarLexema()
        return atualizarTabelaSimbolos(lexema)
    }
}