import scala.util.control.Breaks


/**
 * Created by Christian on 07/08/2014.
 */
 class Alumno{

  private  var matricula:Integer = 0
  private  var carrera =""
  private  var listaMateria:List[String] = List()

  def this(matricula:Integer, carrera:String,listaMateria:List[String]){
    this()
    this.carrera =  carrera
    this.listaMateria =listaMateria
    this.matricula = matricula
  }

  def aprobo: Boolean = {
    var resultado: Boolean = true
    val loop = new Breaks()
    loop.breakable {
      for (i <- listaMateria) {
        if (i.equals("NP")) {
          resultado = false
          loop.break
        } else {
          val x = i.toInt
          if (x < 70) {
            resultado = false
            loop.break
          }
        }

      }
    }
    resultado
  }

}
