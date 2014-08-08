import scala.util.control.Breaks

/**
 * Created by Christian on 07/08/2014.
 */
 class Alumno {

 var matricula:Integer = 0
 var listaMateria: List[Int] = List()

 def aprobo: Boolean = listaMateria.filter( _ < 70).isEmpty

}


trait alumnoNextRow {
  var alumno: Alumno = null
  var nextRowIndex: Integer = 0
}