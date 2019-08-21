package br.ufpe.cin.android.calculadora

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //salva os estados dos textos na tela
        outState?.putCharSequence("text_calc", text_calc.text)
        outState?.putCharSequence("text_info", text_info.text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        //restaura os estados dos textos quando acontece uma mudanca de contexto
        text_calc.setText(savedInstanceState?.getCharSequence("text_calc"))
        text_info.setText(savedInstanceState?.getCharSequence("text_info"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Botão 0
        btn_0.setOnClickListener { view ->
            text_calc.setText(String.format("%s%s", text_calc.text.toString(), "0"))
        }
        //Botão 1
        btn_1.setOnClickListener { view ->
            text_calc.setText(String.format("%s%s", text_calc.text.toString(), "1"))
        }

        //Botão 2
        btn_2.setOnClickListener { view ->
            text_calc.setText(String.format("%s%s", text_calc.text.toString(), "2"))
        }

        //Botão 3
        btn_3.setOnClickListener { view ->
            text_calc.setText(String.format("%s%s", text_calc.text.toString(), "3"))
        }
        //Botão 4
        btn_4.setOnClickListener { view ->
            text_calc.setText(String.format("%s%s", text_calc.text.toString(), "4"))
        }
        //Botão 5
        btn_5.setOnClickListener { view ->
            text_calc.setText(String.format("%s%s", text_calc.text.toString(), "5"))
        }
        //Botão 6
        btn_6.setOnClickListener { view ->
            text_calc.setText(String.format("%s%s", text_calc.text.toString(), "6"))
        }
        //Botão 7
        btn_7.setOnClickListener { view ->
            text_calc.setText(String.format("%s%s", text_calc.text.toString(), "7"))
        }
        //Botão 8
        btn_8.setOnClickListener { view ->
            text_calc.setText(String.format("%s%s", text_calc.text.toString(), "8"))
        }
        //Botão 9
        btn_9.setOnClickListener { view ->
            text_calc.setText(String.format("%s%s", text_calc.text.toString(), "9"))
        }

        //Botão /
        btn_Divide.setOnClickListener { view ->
            text_calc.setText(String.format("%s%s", text_calc.text.toString(), "/"))
        }

        //Botão *
        btn_Multiply.setOnClickListener { view ->
            text_calc.setText(String.format("%s%s", text_calc.text.toString(), "*"))
        }

        //Botão -
        btn_Subtract.setOnClickListener { view ->
            text_calc.setText(String.format("%s%s", text_calc.text.toString(), "-"))
        }

        //Botão +
        btn_Add.setOnClickListener { view ->
            text_calc.setText(String.format("%s%s", text_calc.text.toString(), "+"))
        }

        //Botão (
        btn_LParen.setOnClickListener { view ->
            text_calc.setText(String.format("%s%s", text_calc.text.toString(), "("))
        }

        //Botão )
        btn_RParen.setOnClickListener { view ->
            text_calc.setText(String.format("%s%s", text_calc.text.toString(), ")"))
        }

        //Botão ^
        btn_Power.setOnClickListener { view ->
            text_calc.setText(String.format("%s%s", text_calc.text.toString(), "^"))
        }

        //Botão ^
        btn_Power.setOnClickListener { view ->
            text_calc.setText(String.format("%s%s", text_calc.text.toString(), "^"))
        }

        //Botão .
        btn_Dot.setOnClickListener { view ->
            text_calc.setText(String.format("%s%s", text_calc.text.toString(), "."))
        }

        //Botão =
        btn_Equal.setOnClickListener { view ->

            try {
                text_info.text = eval(text_calc.text.toString()).toString()
            } catch (e: RuntimeException) {
                toast(this, "Expressão inválida")
            }

        }

        //Botão C
        btn_Clear.setOnClickListener { view ->
            text_calc.setText("")
        }

    }

    /**
     * Run toast method
     */
    fun Context.toast(context: Context = applicationContext, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }

    //Como usar a função:
    // eval("2+2") == 4.0
    // eval("2+3*4") = 14.0
    // eval("(2+3)*4") = 20.0
    //Fonte: https://stackoverflow.com/a/26227947
    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '
            fun nextChar() {
                val size = str.length
                ch = if ((++pos < size)) str.get(pos) else (-1).toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Caractere inesperado: " + ch)
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'))
                        x += parseTerm() // adição
                    else if (eat('-'))
                        x -= parseTerm() // subtração
                    else
                        return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'))
                        x *= parseFactor() // multiplicação
                    else if (eat('/'))
                        x /= parseFactor() // divisão
                    else
                        return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor() // + unário
                if (eat('-')) return -parseFactor() // - unário
                var x: Double
                val startPos = this.pos
                if (eat('(')) { // parênteses
                    x = parseExpression()
                    eat(')')
                } else if ((ch in '0'..'9') || ch == '.') { // números
                    while ((ch in '0'..'9') || ch == '.') nextChar()
                    x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
                } else if (ch in 'a'..'z') { // funções
                    while (ch in 'a'..'z') nextChar()
                    val func = str.substring(startPos, this.pos)
                    x = parseFactor()
                    if (func == "sqrt")
                        x = Math.sqrt(x)
                    else if (func == "sin")
                        x = Math.sin(Math.toRadians(x))
                    else if (func == "cos")
                        x = Math.cos(Math.toRadians(x))
                    else if (func == "tan")
                        x = Math.tan(Math.toRadians(x))
                    else
                        throw RuntimeException("Função desconhecida: " + func)
                } else {
                    throw RuntimeException("Caractere inesperado: " + ch.toChar())
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }
}
