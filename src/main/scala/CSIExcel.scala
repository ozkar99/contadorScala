import java.io.{FileWriter, FileOutputStream, File, FileInputStream}
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.apache.poi.ss.usermodel.{Sheet, Cell}
import org.apache.poi.xssf.usermodel.XSSFWorkbook


class CSIExcel(val filePath: String) {

  private val fileIS = new FileInputStream(new File(filePath))
  private val wb = new XSSFWorkbook(fileIS)
  private val swb = new SXSSFWorkbook(wb, 100)
  private val sheet0 = wb.getSheetAt(0)
  private val sheetList: List[Sheet] = createSheetList
  private val lmadList: Set[Integer] = createLmadList


  def process = sheetList.map(processSheet(_, filePath + "_RESULTADOS.txt"))

  private def processSheet(sheet: Sheet, path: String) = {

    /*garbanzo collecziones*/
    Runtime.getRuntime.gc
    println("GARBANZO:\t" + Runtime.getRuntime.freeMemory / 10000)


    //absolute value function.
    def abs(x:Int): Int = if (x < 0) x * -1 else x

    val alumnos = createAlumnoList(sheet)
    val lmadAlumnos = alumnos.filter((x) => lmadList.contains(x.matricula))

    val aprobados = lmadAlumnos.filter(_.aprobo).size //filtra los aprobados, regresa el tamaño.
    val reprobados = abs(lmadAlumnos.size - aprobados) //tamaño real menos aprobados.

    insertarArchivoResultados(sheet.getSheetName, aprobados, reprobados, path)
  }


  private def insertarArchivoResultados(name: String, aprobados: Int, reprobados: Int, path: String) = {
    val fw : FileWriter = new FileWriter(path, true)
    val formattedText = "\r\n\r\n-----------------------------------------------------------\r\n" +
                          name + "\r\n" +
                          "Aprobados: " + aprobados + "\r\n" +
                          "No Aprobados: " + reprobados
    fw.write(formattedText)
    fw.close
  }

  private def createAlumnoList(sheet: Sheet): List[Alumno] = {
    var retval: List[Alumno] = List()
    var iterator = iterateAlumno(sheet, 0)

    retval = iterator.alumno :: retval

    while (iterator.nextRowIndex < sheet.getLastRowNum) {
      iterator = iterateAlumno(sheet, iterator.nextRowIndex)
      retval = iterator.alumno :: retval
    }
    retval
  }

  /*Alumno es lmad*/
  private def isLMAD(alumno: Integer): Boolean = lmadList.contains(alumno)

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
    for (i <- sheet0.getFirstRowNum + 1 to sheet0.getLastRowNum) {
      val row = sheet0.getRow(i)
      val cell = row.getCell(1)
      retval += cell.getNumericCellValue.toInt
    }
    retval
  }

  /*Inserta cantidad de alumnos aprobados y inaprobados en la pagina*/
  private def insertAlumniGrades(sheet: Sheet, aprobados: Integer, noAprobados: Integer) = {
    val aprobadosS = "# Alumnos Aprobados: " + aprobados.toString
    val noAprobadosS = "# Alumnos NO Aprobados: " + noAprobados.toString

    sheet.getRow(0).createCell(3).setCellValue(aprobadosS)
    sheet.getRow(1).createCell(3).setCellValue(noAprobadosS)
  }

  private def extractMatricula(str: String): Integer = str.toCharArray.toList.takeWhile((c) => c != ' ').mkString.toInt

  private def iterateAlumno(sheet: Sheet, rowNum: Integer): alumnoNextRow = {
    var rowReturn = rowNum

    val retval = new Object with alumnoNextRow

    retval.alumno = new Alumno
    retval.alumno.matricula = extractMatricula(sheet.getRow(rowNum).getCell(0).getStringCellValue)

    /*2 jumps*/
    rowReturn += 3

    var row = sheet.getRow(rowReturn)
    while ( row != null && row.getCell(1) != null && row.getCell(0).getCellType != Cell.CELL_TYPE_BLANK) {

      /*If we have data read it*/
      if (row.getCell(5) != null) {
        val cell = row.getCell(5)
        if (cell.getCellType == Cell.CELL_TYPE_NUMERIC)
          retval.alumno.listaMateria = cell.getNumericCellValue.toInt :: retval.alumno.listaMateria
        else
          retval.alumno.listaMateria = 0 :: retval.alumno.listaMateria
      }

      /*increment the while loop*/
      rowReturn += 1
      row = sheet.getRow(rowReturn)
    }
    retval.nextRowIndex = rowReturn
    retval
  }


}