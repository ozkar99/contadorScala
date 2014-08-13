 class Alumno {
 var matricula:Int = 0
  /*por default el alumno aprueba*/
 var aprobo: Boolean = true
}


 /*Simple wrapper with alumno + nextRow, could have used a tuple, meh...*/
trait alumnoNextRow {
  var alumno: Alumno = null
  var nextRowIndex: Integer = 0
}