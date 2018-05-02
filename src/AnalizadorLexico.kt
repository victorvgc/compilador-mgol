import utils.Saida
import java.io.CharArrayReader

class AnalizadorLexico (val tabelaSimbolos: HashMap<String, Lexema>, input: String){

    //val test: String = "3 - 4 + (-1) - (-0)"
    //val charArray = test.toCharArray()

    val charArray = input.toCharArray()

    val charArrayReader: CharArrayReader = CharArrayReader(charArray)

    var linhaAtual = 1
    var colunaAtual = 1

    private fun analizarLexema(): Lexema{
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

    fun nextLexema(): Lexema {
        val afdLexico = AFDLexicoHashMap()

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
            if (saida != Saida.SEM_TRANSICAO  && saida != Saida.ACEITO)
                lexema += char
        }while (saida == Saida.PROCESSANDO)

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
            return nextLexema()

        if (saida != Saida.ACEITO)
            return Erro(char.toString(), linhaAtual, colunaAtual)
        return Lexema(afdLexico.ultimoEstadoAlcancado, lexema)
    }

    private fun alreadyIsOnTabelaSimbolos (lexema: Lexema): Boolean {
        val token: Lexema? = tabelaSimbolos[lexema.lexema]

        return token != null
    }

    private fun atualizarTabelaSimbolos(lexema: Lexema): Lexema {

        if (!alreadyIsOnTabelaSimbolos(lexema) && lexema.token.equals("id"))
            tabelaSimbolos[lexema.lexema] = lexema

        else if (lexema.token.equals("id")) {
            tabelaSimbolos[lexema.lexema]!!.update(lexema)

            return tabelaSimbolos[lexema.lexema]!!
        }

        return lexema
    }

    fun analizar(): Lexema {
        val lexema = analizarLexema()
        return atualizarTabelaSimbolos(lexema)
    }

    fun getNextLexema(): Lexema {
        val lexema = nextLexema()
        return atualizarTabelaSimbolos(lexema)
    }
}