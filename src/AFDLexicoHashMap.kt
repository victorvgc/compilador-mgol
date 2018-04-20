import utils.Saida

class AFDLexicoHashMap {

    private var estadoAtual: Int = 0
    private val estadosFinais: IntArray = intArrayOf(1,2,4,7,9,10,12,14,15,16,17,18,19,20,21,22)
    private var ultimoEstadoAlcancado : Int = -1

    init {
        inicializaAfd()
    }

    fun reset(){
        estadoAtual = 0
    }

    fun processar (entrada : Char): Int {
        val chave: String = entrada.toString() + estadoAtual

        if (entrada.toInt() == 0xFFFF)
            estadoAtual = getEstado("EOF$estadoAtual")
        else if(entrada.isDigit())
            estadoAtual = getEstado("D$estadoAtual")
        else if (entrada.isLetter() && estadoAtual != 2 && estadoAtual != 4)
            estadoAtual = getEstado("L$estadoAtual")
        else if (entrada.toInt() == 9)
            estadoAtual = getEstado("TAB$estadoAtual")
        else if (entrada.toInt() == 10 || entrada.toInt() == 13)
            estadoAtual = getEstado("SALTO$estadoAtual")
        else if (entrada.equals('"'))
            estadoAtual = getEstado("ASPAS$estadoAtual")
        else
            estadoAtual = getEstado(chave)

        if (estadoAtual != -1) {
            ultimoEstadoAlcancado = estadoAtual
        }
        var status: Int

        if (estadoAtual == -1) {
            status = if (isCadeiaValida())
                Saida.ACEITO
            else
                Saida.ERRO
            reset()
        }
        else
            status = Saida.PROCESSANDO

        return status
    }

    private fun getEstado (estadoEntrada : String) : Int  {

        return if (afdHashMap[estadoEntrada] != null)
            afdHashMap[estadoEntrada]!!
        else
            -1
    }

    fun isCadeiaValida (): Boolean {
        return estadosFinais.contains(ultimoEstadoAlcancado)
    }


    val afdHashMap = HashMap<String, Int>()

    private fun inicializaAfd() {
        afdHashMap["D0"] = 2
        afdHashMap["L0"] = 10
        afdHashMap["+0"] = 1
        afdHashMap["-0"] = 1
        afdHashMap["*0"] = 1
        afdHashMap["/0"] = 1
        afdHashMap[">0"] = 17
        afdHashMap["<0"] = 15
        afdHashMap["=0"] = 16
        afdHashMap["ASPAS0"] = 8
        afdHashMap["{0"] = 11
        afdHashMap["EOF0"] = 14
        afdHashMap["(0"] = 20
        afdHashMap[")"] = 21
        afdHashMap[" 0"] = 13
        afdHashMap[";0"] = 22

        afdHashMap["D1"] = 2

        afdHashMap["D2"] = 2
        afdHashMap[".2"] = 3

        afdHashMap["D3"] = 4

        afdHashMap["D4"] = 4
        afdHashMap["E4"] = 5

        afdHashMap["D5"] = 7
        afdHashMap["+5"] = 6
        afdHashMap["-5"] = 6

        afdHashMap["D6"] = 7

        afdHashMap["D7"] = 7

        afdHashMap["D8"] = 8
        afdHashMap["L8"] = 8
        afdHashMap[".8"] = 8
        afdHashMap["+8"] = 8
        afdHashMap["-8"] = 8
        afdHashMap["*8"] = 8
        afdHashMap["/8"] = 8
        afdHashMap[">8"] = 8
        afdHashMap["<8"] = 8
        afdHashMap["=8"] = 8
        afdHashMap["TAB8"] = 8
        afdHashMap["SALTO8"] = 8
        afdHashMap["ASPAS8"] = 8
        afdHashMap["{8"] = 8
        afdHashMap["}8"] = 8
        afdHashMap["_8"] = 8
        afdHashMap["E8"] = 8
        afdHashMap["(8"] = 8
        afdHashMap[")8"] = 8
        afdHashMap[" 8"] = 8
        afdHashMap[";8"] = 8

        afdHashMap["D10"] = 10
        afdHashMap["L10"] = 10
        afdHashMap["_10"] = 10

        afdHashMap["D11"] = 11
        afdHashMap["L11"] = 11
        afdHashMap[".11"] = 11
        afdHashMap["+11"] = 11
        afdHashMap["-11"] = 11
        afdHashMap["*11"] = 11
        afdHashMap["/11"] = 11
        afdHashMap[">11"] = 11
        afdHashMap["<11"] = 11
        afdHashMap["=11"] = 11
        afdHashMap["TAB11"] = 11
        afdHashMap["SALTO11"] = 11
        afdHashMap["ASPAS11"] = 11
        afdHashMap["{11"] = 11
        afdHashMap["}11"] = 12
        afdHashMap["_11"] = 11
        afdHashMap["E11"] = 11
        afdHashMap["(11"] = 11
        afdHashMap[")11"] = 11
        afdHashMap[" 11"] = 11
        afdHashMap[";11"] = 11

        afdHashMap["-15"] = 18
        afdHashMap[">15"] = 16
        afdHashMap["=15"] = 16

        afdHashMap["=17"] = 16
    }
}