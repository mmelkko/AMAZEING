package game.results;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * ResultTable reads from the file the results there are and create ResultRows
 * out of them, comparing them and the possible new ones and setting them to
 * order, before making it available to save them to a file back again.
 * 
 * @see ResultRow
 * 
 * @extends AbstractTableModel
 * 
 * @author Merituuli Melkko
 * */
public class ResultTable extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	/**Names of the columns*/
	private static final String[] columns = {"N:o", "Name", "Date", 
	"Points"};
	private List<ResultRow> resultrows;
	private int size;
	private String fileName;

	/**
	 * @param fileName that is the fileName from which the ResultTable is made
	 * @param size that is the maximum amount of ResultRows this will hold
	 * */
	public ResultTable(String fileName, int size){
		this.resultrows = new ArrayList<ResultRow>();
		this.size = size;
		this.fileName = fileName;

		try {
			BufferedReader resultreader
			= new BufferedReader(new FileReader(this.fileName));
			try {
				String tulos = resultreader.readLine();
				while (tulos != null){
					String[] osat = tulos.split("&");
					if (osat.length == 3){
						try {
							this.addRow(new ResultRow(osat[0], 
									new SimpleDateFormat("dd.MM.yyyy")
							.parse(osat[1]), Integer.parseInt(osat[2])));
						} catch (ParseException e) {
							System.err.println("Parseing the results failed.");
						}
					} else {
						System.err.println("ResultRow wasn't of right length!");
					}
					tulos = resultreader.readLine();
				}
				if (resultreader != null){
					resultreader.close();
				}
			} catch (IOException e) {
				System.err.println("An error while reading the resultfile.");
			}
		} catch (FileNotFoundException e) {
			System.err.println("THERE IS NO RESULTFILE.");
		}
	}

	/**
	 * Adds the row to the list of the rows and cuts the list to the right
	 * length
	 * 
	 * @param row that is added
	 * 
	 * @return int of the position that the row was added to or -1 if the
	 * row was added then deleted from the list
	 * */
	public int addRow(ResultRow row){ 
		this.resultrows.add(row);
		Collections.sort(this.resultrows);

		int place = this.resultrows.indexOf(row)+1;
		if (place > this.size){
			place = -1;
		}
		/*while-lause jos esim. tiedostosta luettaessa resultrows-listaan
		 *on tallennettu enemmän kuin this.koko tulosrow:tä :) muutenhan if
		 *riittäisi, mutta varmistuksia varmistuksia...*/
		while (this.resultrows.size() > this.size){
			this.resultrows.remove(this.resultrows.size()-1);
		}

		return place;
	}

	/**
	 * @return Iterator<ResultRow>
	 * */
	public Iterator<ResultRow> iterator(){
		return this.resultrows.iterator();
	}

	/**
	 * Saves all the ResultRows in the list to the fileName that was specified 
	 * in the constructor
	 * 
	 * @return boolean, that tells if the saving went well
	 * */
	public boolean saveRowsToFile(){ 
		try{
			BufferedWriter saver = new BufferedWriter(new FileWriter
					(this.fileName));
			Iterator<ResultRow> iteraattori = this.iterator();
			while (iteraattori.hasNext()){
				ResultRow row = iteraattori.next();
				saver.write(row.toString() + "\n");
			}
			if (saver != null){			
				saver.close(); 
			}
			return true;
		}catch (IOException e){
			System.err.println("Writing to the file failed.");
			return false;
		}
	}

	/**
	 * @return int number of columns
	 * */
	@Override
	public int getColumnCount(){
		return 4;
	}

	/**
	 * @return int number of rows
	 * */
	@Override
	public int getRowCount(){
		return this.resultrows.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (column < 0 || column >= this.getColumnCount() || row < 0 
				|| row >= this.getRowCount()){
			return null;

		} else if (column == 0){
			return row+1 + ".";

		} else {
			ResultRow resultrow = this.resultrows.get(row);
			String[] results = resultrow.toString().split("&");
			return results[column-1];
		}
	}

	@Override
	public String getColumnName(int column){
		return ResultTable.columns[column];
	}
}
