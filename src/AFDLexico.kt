import utils.Entrada
import utils.MatrizEstados
import utils.Saida

class AFDLexico {
    private var estado: Int = 0
    var ultimoEstadoAlcancado: Int = -1

    private val estadosFinais: IntArray = intArrayOf(1,2,4,7,9,10,12,14,15,16,17,18,19,20,21,22)
    private val matrizEstados: MatrizEstados = MatrizEstados()

    fun processar(entrada: Char): Int {
        if (entrada.toInt() == 0xFFFF) {
            estado = matrizEstados[estado, Entrada.EOF]!!
        }
        else if (entrada.isDigit()) {
            estado = matrizEstados[estado, Entrada.DIGITO]!!
        }
        else if (entrada.isLetter() && estado != 2 && estado != 4) {
            estado = matrizEstados[estado, Entrada.LETRA]!!
        }
        else if (entrada.equals('.')) {
            estado = matrizEstados[estado, Entrada.PONTO]!!
        }
        else if (entrada.equals('+')) {
            estado = matrizEstados[estado, Entrada.MAIS]!!
        }
        else if (entrada.equals('-')) {
            estado = matrizEstados[estado, Entrada.MENOS]!!
        }
        else if (entrada.equals('*')) {
            estado = matrizEstados[estado, Entrada.MULTI]!!
        }
        else if (entrada.equals('/')) {
            estado = matrizEstados[estado, Entrada.DIV]!!
        }
        else if (entrada.equals('>')) {
            estado = matrizEstados[estado, Entrada.MAIOR]!!
        }
        else if (entrada.equals('<')) {
            estado = matrizEstados[estado, Entrada.MENOR]!!
        }
        else if (entrada.equals('=')) {
            estado = matrizEstados[estado, Entrada.IGUAL]!!
        }
        else if (entrada.toInt() == 9) {
            estado = matrizEstados[estado, Entrada.TAB]!!
        }
        else if (entrada.toInt() == 10 || entrada.toInt() == 13) {
            estado = matrizEstados[estado, Entrada.NOVA_LINHA]!!
        }
        else if (entrada.equals('"')) {
            estado = matrizEstados[estado, Entrada.ASPAS]!!
        }
        else if (entrada.equals('{')) {
            estado = matrizEstados[estado, Entrada.ABRE_CHAVE]!!
        }
        else if (entrada.equals('}')) {
            estado = matrizEstados[estado, Entrada.FECHA_CHAVE]!!
        }
        else if (entrada.equals('_')) {
            estado = matrizEstados[estado, Entrada.UNDERLINE]!!
        }
        else if (entrada.equals('E')) {
            estado = matrizEstados[estado, Entrada.EXP]!!
        }
        else if (entrada.equals('(')) {
            estado = matrizEstados[estado, Entrada.ABRE_PARENTESE]!!
        }
        else if (entrada.equals(')')) {
            estado = matrizEstados[estado, Entrada.FECHA_PARENTESE]!!
        }
        else if (entrada.equals(' ')) {
            estado = matrizEstados[estado, Entrada.ESPACO]!!
        }
        else if (entrada.equals(';')) {
            estado = matrizEstados[estado, Entrada.PONTO_VIGULA]!!
        }
        else if (isSpecial(entrada)) {
            estado = matrizEstados[estado, Entrada.ESPECIAL]!!
        }
        else
            return Saida.ERRO

        if (estado != -1) {
            ultimoEstadoAlcancado = estado
        }
        var status: Int

        if (estado == -1) {
            status = Saida.SEM_TRANSICAO
            reset()
        }
        else
            status = Saida.PROCESSANDO

        if (estado == 12 || estado == 13) {
            status = Saida.IGNORAR
        }


        return status
    }

    private fun reset() {
        estado = 0
    }

    fun isCadeiaValida (): Boolean {
        return estadosFinais.contains(ultimoEstadoAlcancado)
    }

    private fun isSpecial(char: Char): Boolean {
        return !char.isLetterOrDigit()
    }
}
