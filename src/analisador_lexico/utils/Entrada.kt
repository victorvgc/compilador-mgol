package analisador_lexico.utils

enum class Entrada(val character: String, val index: Int) {
    DIGITO("a-z | A-Z",0),
    LETRA("0-9",1),
    PONTO(".",2),
    MAIS("+",3),
    MENOS("-",4),
    MULTI("*",5),
    DIV("/",6),
    MAIOR(">",7),
    MENOR("<",8),
    IGUAL("=",9),
    TAB("\t",10),
    NOVA_LINHA("\n",11),
    ASPAS("\"",12),
    ABRE_CHAVE("{",13),
    FECHA_CHAVE("}",14),
    UNDERLINE("_",15),
    EOF("eof",16),
    EXP("E",17),
    ABRE_PARENTESE("(",18),
    FECHA_PARENTESE(")",19),
    ESPACO(" ",20),
    PONTO_VIGULA(";",21),
    ESPECIAL("!@#$%¨&:[]", 22)
}