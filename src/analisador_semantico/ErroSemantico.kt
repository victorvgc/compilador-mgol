package analisador_semantico

import analisador_lexico.Lexema

class ErroSemantico (val erro : String, val linhaColuna : String) {

    override fun toString(): String {
        return "Erro Semântico: $erro. ${linhaColuna}"
    }
}