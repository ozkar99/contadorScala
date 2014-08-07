import java.io.{FileOutputStream, File, FileInputStream}
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.ss.usermodel.{Sheet, Cell}
import scala.annotation.tailrec




class CSIExcel(val filePath: String) {

  private val fileIS = new FileInputStream(new File(filePath))

  private val wb = new XSSFWorkbook(fileIS)
  private val sheet0 = wb.getSheetAt(0)
  private val sheetList : List[Sheet] = createSheetList
  private val lmadList: Set[Integer] = createLmadList

  def process = {

    innerprocess(sheetList) //process file.
    wb.write(new FileOutputStream(new File(filePath))) //write file.

    @tailrec
    def innerprocess(l: List[Sheet]): Boolean = {
      if(l.isEmpty) /*break condition*/
        true
      else {
        /*Do sheet processing*/


        /*recursive call*/
        innerprocess(l.tail)
      }
    }
  }



  /*Alumno es lmad*/
  private def isLMAD(alumno: Integer): Boolean = lmadList.contains(alumno)

  /*Crea nuestra lista de hojas para procesar*/
  private def createSheetList: List[Sheet] = {
    var retval : List[Sheet] = List()
    val sheetNo = wb.getNumberOfSheets
    for( i <- 1 until sheetNo) {
      retval = wb.getSheetAt(i) :: retval
    }
    retval
  }

  /*Construye set de matriculas de lmad*/
  private def createLmadList: Set[Integer] = {
    var retval: Set[Integer] = Set()
    for( i <- sheet0.getFirstRowNum+1 to sheet0.getLastRowNum) {
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

}