import java.io.{FileWriter, File, FileInputStream}
import org.apache.poi.ss.usermodel.{WorkbookFactory, Sheet, Cell}

import scala.collection.mutable.UnrolledBuffer


class CSIExcel(val filePath: String) {


  private val fileIS = new FileInputStream(new File(filePath))
  private val wb = WorkbookFactory.create(fileIS)
  private val sheet0 = wb.getSheetAt(0)
  private val sheetList: List[Sheet] = createSheetList
  private val lmadList: Set[Integer] = createLmadList
  private var progress = 1


  def process = sheetList.foreach( x=> processSheet(x, filePath + "_RESULTADOS.txt") )

  private def processSheet(sheet: Sheet, path: String) = {

    //absolute value function.
    def abs(x:Int): Int = if (x < 0) x * -1 else x

    val alumnos = createAlumnoList(sheet)
    val lmadAlumnos = alumnos.filter( x=> lmadList.contains(x.matricula))

    val aprobados = lmadAlumnos.filter(_.aprobo).size //filtra los aprobados, regresa el tamaño.
    val reprobados = abs(lmadAlumnos.size - aprobados) //tamaño real menos aprobados.

    insertarArchivoResultados(sheet.getSheetName, aprobados, reprobados, path)
    progress += 1
  }


  /*imprime resultado en archivo de texto*/
  private def insertarArchivoResultados(name: String, aprobados: Int, reprobados: Int, path: String) = {
    val fw : FileWriter = new FileWriter(path, true)
    val formattedText = "\r\n\r\n-----------------------------------------------------------\r\n" +
                          name + "\r\n" +
                          "Aprobados: " + aprobados + "\r\n" +
                          "No Aprobados: " + reprobados
    fw.write(formattedText)
    fw.close
  }

  /*crea la lista de alumnos*/
  private def createAlumnoList(sheet: Sheet): UnrolledBuffer[Alumno] = {
    var retval: UnrolledBuffer[Alumno] = UnrolledBuffer()

    var iterator = iterateAlumno(sheet, 0)
    retval += iterator.alumno

    while (iterator.nextRowIndex < sheet.getLastRowNum) {
      iterator = iterateAlumno(sheet, iterator.nextRowIndex)
      retval += iterator.alumno
    }
    retval
  }

  /*Crea nuestra lista de hojas para procesar*/
  private def createSheetList: List[Sheet] = {
    var retval: List[Sheet] = List()
    val sheetNo = wb.getNumberOfSheets
    for (i <- sheetNo-1 to 1 by -1) {
      retval = wb.getSheetAt(i) :: retval
    }
    retval
  }

  /*Construye set de matriculas de lmad*/
  private def createLmadList: Set[Integer] = {
    var retval: Set[Integer] = Set()

    for (i <- sheet0.getFirstRowNum to sheet0.getLastRowNum) {
      val row = sheet0.getRow(i)

      if( row != null && row.getCell(0).getCellType == Cell.CELL_TYPE_NUMERIC) {
        val cell = row.getCell(0)
        retval += cell.getNumericCellValue.toInt
      }
    }

    retval
  }

  /*Extrae la matricula del nombre del alumno*/
  private def extractMatricula(str: String): Integer = str.toCharArray.toList.takeWhile((c) => c != ' ').mkString.toInt

  /*Itera alumno, regresa el alumno y el numero de fila del siguiente alumno*/
  private def iterateAlumno(sheet: Sheet, rowNum: Integer): alumnoNextRow = {
    var rowReturn = rowNum

    val retval = new Object with alumnoNextRow

    retval.alumno = new Alumno
    retval.alumno.matricula = extractMatricula(sheet.getRow(rowNum).getCell(0).getStringCellValue)

    /*2 jumps from where we are*/
    rowReturn += 3

    var row = sheet.getRow(rowReturn)

    while ( row != null && row.getCell(1) != null && row.getCell(0).getCellType != Cell.CELL_TYPE_BLANK) {
      if (row.getCell(5) != null) {
        val cell = row.getCell(5)

        if (
            (cell.getCellType == Cell.CELL_TYPE_NUMERIC && cell.getNumericCellValue < 70) //menor a 70
            || cell.getCellType == Cell.CELL_TYPE_BLANK  //si esta vacia
            || cell.getCellType == Cell.CELL_TYPE_STRING ) //si es "NP"

          retval.alumno.aprobo = false;

      }

      /*increment the while loop*/
      rowReturn += 1
      row = sheet.getRow(rowReturn)
    }
    retval.nextRowIndex = rowReturn
    retval
  }


}