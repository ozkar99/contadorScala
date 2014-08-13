 class Alumno {

 var matricula:Integer = 0
 var listaMateria: List[Int] = List()

  /*por default el alumno aprueba*/
 var aprobo: Boolean = true

}


 /*Simple wrapper with alumno + nextRow, could have used a tuple, meh...*/
trait alumnoNextRow {
  var alumno: Alumno = null
  var nextRowIndex: Integer = 0
}