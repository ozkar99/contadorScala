import java.io.{FileOutputStream, File, FileInputStream}
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.ss.usermodel.{Row, Sheet, Cell}
import scala.annotation.tailrec


class CSIExcel(val filePath: String) {

  private val fileIS = new FileInputStream(new File(filePath))
  private val wb = new XSSFWorkbook(fileIS)
  private val sheet0 = wb.getSheetAt(0)
  private val sheetList: List[Sheet] = createSheetList
  private val lmadList: Set[Integer] = createLmadList
  private val util : Utilerias = new Utilerias
  private var alumnosList: List[Alumno] = List()


  def process = {

    innerprocess(sheetList) //process file.
    wb.write(new FileOutputStream(new File(filePath))) //write file.


  }


  @tailrec
  private def innerprocess(l: List[Sheet]): Boolean = {
    if (l.isEmpty) /*break condition*/
      true
    else {
      /*Do sheet processing*/
      var sheet:Sheet = l.head
      processSheet(sheet, sheet.getFirstRowNum)
      /*recursive call*/
      innerprocess(l.tail)
    }
  }


  private def processSheet(sheet:Sheet, rowIndex: Integer) {

    if ((rowIndex <= sheet.getLastRowNum)){


      var currentMatricula: String = ""
      val currentCell: Cell = sheet.getRow(rowIndex).getCell(0)
      val currentCalificacionCell: Cell = sheet.getRow(rowIndex).getCell(5)

      if (currentCell.getCellType == Cell.CELL_TYPE_STRING) {
        currentMatricula = extractMatricula(currentCell.getStringCellValue)
        println(currentMatricula)
        processSheet(sheet, rowIndex + 1)
      } else if (currentCell.getCellType == Cell.CELL_TYPE_NUMERIC) {
        println(currentCell.getNumericCellValue)
        processSheet(sheet, rowIndex + 1)
      }
    }
  }






  /*private def processSheet(sheet: Sheet) = {
    var rowIndex=0
    var jump=0
    var currentMatricula:String = ""
    var currentCell = sheet.getRow(rowIndex).getCell(0)

    if(currentCell.getCellType == Cell.CELL_TYPE_STRING ) {
      currentMatricula = extractMatricula(currentCell.getStringCellValue)
      rowIndex += 2

      if (sheet.getRow(rowIndex).getCell(0).getCellType == Cell.CELL_TYPE_NUMERIC) {
          val foo = sheet.getRow(rowIndex).getCell(5).getStringCellValue

      }

    } else {

    }
      rowIndex += jump
  }*/

  /*Alumno es lmad*/
  private def isLMAD(alumno: Integer): Boolean = lmadList.contains(alumno)

  /*Crea nuestra lista de hojas para procesar*/
  private def createSheetList: List[Sheet] = {
    var retval: List[Sheet] = List()
    val sheetNo = wb.getNumberOfSheets
    for (i <- 1 until sheetNo) {
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

  private def extractMatricula(str: String): String = str.toCharArray.toList.takeWhile((c) => c != ' ').mkString.toString

  private def iterateAlumno(sheet: Sheet, rowNum: Integer): myTupla = {
    var rowNum = 0

    val retval = new Object with myTupla
    retval.alumno = new Alumno
    retval.nextRowIndex = 10


    retval
  }

  trait myTupla {
    var alumno: Alumno = null
    var nextRowIndex: Integer = 0
  }


}