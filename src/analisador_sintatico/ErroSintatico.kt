package analisador_sintatico

import analisador_lexico.Lexema

class ErroSintatico (estadoErro: String, val simboloErro: Lexema, val linhaColuna: String){
    val simboloEsperado: String

    init {
        simboloEsperado = when (estadoErro.toInt()){
            0 -> "'inicio'"
            1, 5, 9, 19, 20, 21 -> "fim do arquivo"
            2 -> "'varinicio'"
            3, 6, 7, 8, 15, 28, 35, 36 -> "'id', 'leia', 'escreva', 'se' ou 'fim'"
            4, 16, 34 -> "'id' ou 'varfim'"
            10 -> "'id'"
            11 -> "'literal', 'num' ou 'id'"
            12 -> "'<-'"
            13, 29, 30, 31, 57 -> "'id', 'leia', 'escreva', 'se' ou 'fimse'"
            14 -> "'('"
            17, 22, 23, 24, 25, 26, 37, 38, 39, 40, 43, 58 -> "';'"
            18 -> "'int', 'real' ou 'lit'"
            27, 33, 52, 55 -> "'id' ou 'num'"
            32, 41, 42, 47, 48, 49, 54 -> "'id', 'leia', 'escreva', 'se', 'fimse' ou 'fim'"
            44 -> "'+', '-', '*', '/' ou ';'"
            45, 46 -> "'+', '-', '*', '/', '>', '<', '<=', '>=', '<>', '=', ')' ou ';'"
            50, 53 -> "')'"
            51 -> "'>', '<', '<=', '>=', '<>' ou '='"
            56 -> "'entao'"
            else -> "desconhecido"
        }
    }

    override fun toString(): String {
        return "Erro Sintático. Simbolo esperado: $simboloEsperado. Símbolo encontrado: '${simboloErro.lexema}'. $linhaColuna."
    }
}