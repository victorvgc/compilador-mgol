package analisador_semantico

import analisador_lexico.Lexema
import java.io.File
import java.io.FileWriter

class AnalisadorSemantico (val programaObjeto : File, val tabelaSimbolos : HashMap<String, Lexema>) {

    private var tempCount : Int = 1
    private var tempVarBuffer = StringBuilder()
    private var programBodyBuffer = StringBuilder()
    private var programHeadBuffer = StringBuilder()

    init {
        programHeadBuffer.append("#include <stdio.h>\n")
        programHeadBuffer.append("typedef char lit[256];\n")
        programHeadBuffer.append("void main(void) {\n")
    }

    fun analisarSemantica (reducao : String, params : HashMap<String, Lexema>) : Lexema {

        return when (reducao) {

            "LV" -> {
                Lexema(reducao, reducao)
            }

            "D" -> {
                params["id"]!!.tipo = params["TIPO"]!!.tipo

                atualizarTabelaSibolos(params["id"]!!)

                programBodyBuffer.append("${params["TIPO"]!!.tipo} ${params["id"]!!.lexema};\n")

                Lexema(reducao, reducao)
            }

            "TIPO" -> {
                val key = params.keys

                Lexema(reducao, reducao, params[key.first().toString()]!!.tipo)
            }

            "ES" -> {
                val key = params.keys

                if (key.contains("id")) {
                    when (params["id"]!!.tipo) {
                        "lit" -> programBodyBuffer.append("scanf(\"%s\", ${params["id"]!!.lexema});\n")
                        "int" -> programBodyBuffer.append("scanf(\"%d\", &${params["id"]!!.lexema});\n")
                        "double" -> programBodyBuffer.append("scanf(\"%lf\", &${params["id"]!!.lexema});\n")
                        else -> Lexema("ERRO SMT", "Variavel não declarada")
                    }
                }
                else
                    programBodyBuffer.append("printf(${params["ARG"]!!.lexema})\n")

                Lexema(reducao, reducao)
            }

            "ARG" -> {
                val key = params.keys

                if (key.first().equals("id")) {
                    if (isAlreadyOnTabelaSimbolos(params["id"]!!))
                        Lexema(reducao, params["id"]!!.lexema, params["id"]!!.tipo)
                    else
                        Lexema("ERRO SMT", "Variavel não declarada")
                }
                else {
                    Lexema(reducao, params[key.first()]!!.lexema, params[key.first()]!!.tipo)
                }
            }

            "CMD" -> {
                if (isAlreadyOnTabelaSimbolos(params["id"]!!)) {
                    if (params["id"]!!.tipo == params["LD"]!!.tipo) {
                        programBodyBuffer.append("${params["id"]!!.lexema} ${params["rcb"]!!.tipo} ${params["LD"]!!.lexema};\n")
                        Lexema(reducao, reducao)
                    }
                    else
                        Lexema("ERRO SMT", "Tipo da variavel de atribuição incompatível")
                }
                else
                    Lexema("ERRO SMT", "Variavel não declarada")
            }

            "LD" -> {
                val key = params.keys

                if (key.contains("opm")) {
                    val oprd1 = params["OPRD1"]!!
                    val oprd2 = params["OPRD2"]!!

                    if (oprd1.tipo == oprd2.tipo && !oprd1.tipo.equals("lit")) {
                        tempVarBuffer.append("${oprd1.tipo} t${tempCount};\n")
                        programBodyBuffer.append("t${tempCount} = ${oprd1.lexema} ${params["opm"]!!.tipo} ${oprd2.lexema}\n")

                        tempCount++

                        Lexema(reducao, "t${tempCount-1}", oprd1.tipo)
                    } else
                        Lexema("ERRO SMT", "Operandos com tipos incompatíveis")
                }
                else {
                    Lexema(reducao, params["OPRD2"]!!.lexema, params["OPRD2"]!!.tipo)
                }
            }

            "OPRD" -> {
                val key = params.keys

                if (key.contains("id")) {
                    if (isAlreadyOnTabelaSimbolos(params["id"]!!))
                        Lexema(reducao, params["id"]!!.lexema, params["id"]!!.tipo)
                    else
                        Lexema("ERRO SMT", "Variavel não declarada")
                }
                else
                    Lexema(reducao, params["num"]!!.lexema, params["num"]!!.tipo)
            }

            "COND" -> {
                programBodyBuffer.append("}")

                Lexema(reducao, reducao)
            }

            "CABECALHO" -> {
                programBodyBuffer.append("if (${params["EXP_R"]!!.lexema}) {")

                Lexema(reducao, reducao)
            }

            "EXP_R" -> {
                val oprd1 = params["OPRD1"]!!
                val oprd2 = params["OPRD2"]!!

                if (oprd1.tipo == oprd2.tipo && !oprd1.tipo.equals("lit")) {
                    tempVarBuffer.append("${oprd1.tipo} t${tempCount};\n")
                    programBodyBuffer.append("t${tempCount} = ${oprd1.lexema}${params["opr"]!!.tipo}${oprd2.lexema}\n")

                    tempCount++

                    Lexema(reducao, "t${tempCount-1}", oprd1.tipo)
                } else
                    Lexema("ERRO SMT", "Operandos com tipos incompatíveis")
            }

            else -> {
                Lexema(reducao, reducao)
            }
        }
    }

    private fun atualizarTabelaSibolos (lexema : Lexema) {
        tabelaSimbolos[lexema.lexema]!!.update(lexema)
    }

    private fun isAlreadyOnTabelaSimbolos (lexema: Lexema): Boolean {
        val token: Lexema? = tabelaSimbolos[lexema.lexema]

        return token != null
    }

    fun buildProgram() {
        val programBuilder = StringBuilder()

        programBuilder.append(programHeadBuffer)
        programBuilder.append("/*----Variaveis Temporarias----*/\n")
        programBuilder.append(tempVarBuffer)
        programBuilder.append("/*-----------------------------*/\n")
        programBuilder.append(programBodyBuffer)
        programBuilder.append("}")

        programaObjeto.writeText(programBuilder.toString())
    }

}