import java.io.File
import java.io.InputStream

fun main (args: Array<String>) {
    //Inicializa a tabela de simbolos
    val tabelaSimbolos = inicializarTabelaSimbolos()

    val inputStream: InputStream = File("Testes\\teste.txt").inputStream()
    val inputString = inputStream.bufferedReader().use { it.readText() }

    val analizadorLexico = AnalizadorLexico(tabelaSimbolos, inputString)

    do {
        val lexema = analizadorLexico.getNextLexema()
        println(lexema.toString())
    }while (!lexema.token.equals("EOF") && !lexema.token.equals("ERRO"))
}

/**
 * Inicializa a tabela de simbolos com as palavras reservadas da linguagem
 */
fun inicializarTabelaSimbolos(): HashMap<String, Lexema> {
    //key: lexema, object: Lexema
    val tabelaSimbolos: HashMap<String, Lexema> = HashMap()

    tabelaSimbolos["inicio"] = object: Lexema("inicio", "inicio"){}
    tabelaSimbolos["varinicio"] = object: Lexema("varinicio", "varinicio"){}
    tabelaSimbolos["varfim"] = object: Lexema("varfim", "varfim"){}
    tabelaSimbolos["escreva"] = object: Lexema("escreva", "escreva"){}
    tabelaSimbolos["leia"] = object: Lexema("leia", "leia"){}
    tabelaSimbolos["se"] = object: Lexema("se", "se"){}
    tabelaSimbolos["entao"] = object: Lexema("entao", "entao"){}
    tabelaSimbolos["senao"] = object: Lexema("senao", "senao"){}
    tabelaSimbolos["fimse"] = object: Lexema("fimse", "fimse"){}
    tabelaSimbolos["fim"] = object: Lexema("fim", "fim"){}
    tabelaSimbolos["inteiro"] = object: Lexema("inteiro", "inteiro"){}
    tabelaSimbolos["literal"] = object: Lexema("literal", "literal"){}
    tabelaSimbolos["real"] = object: Lexema("real", "real"){}

    return tabelaSimbolos
}