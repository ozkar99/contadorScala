 class Alumno {

 var matricula:Integer = 0
 var listaMateria: List[Int] = List()

  /*checa las materias, si tiene una con menos de 70, se considera reprobado*/
 def aprobo: Boolean = listaMateria.filter( _ < 70).isEmpty

}


 /*Simple wrapper with alumno + nextRow, could have used a tuple, meh...*/
trait alumnoNextRow {
  var alumno: Alumno = null
  var nextRowIndex: Integer = 0
}