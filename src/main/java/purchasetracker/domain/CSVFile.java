package purchasetracker.domain;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import purchasetracker.Constants;


public class CSVFile {

	public static final String DEFAULT_SEPARATOR = ",";

	private final InputStream inputStream;
	private final Map<String, Header> headers;
	private final AtomicBoolean firstLineIsHeader;
	private final List<Row> rows;
	private final AtomicBoolean fileRead;

	public CSVFile(InputStream inputStream, String... headers) {
		this(inputStream);
		this.firstLineIsHeader.set(false);

		for (int i = 0; i < headers.length; i++) {
			this.headers.put(headers[i], new Header(i, headers[i]));
		}

	}

	public CSVFile(InputStream inputStream) {
		
		this.inputStream = inputStream;
		this.headers = new HashMap<>();
		this.firstLineIsHeader = new AtomicBoolean(true);
		this.fileRead = new AtomicBoolean(false);
		this.rows = new ArrayList<>();
	}

	public List<String> getHeaders() {

		if (!fileRead.get()) {
			read();
		}

		List<String> headerNames = new ArrayList<>(Collections.nCopies(headers.size(), (String)null));

		for (Header header : headers.values()) {
			headerNames.set(header.getIndex(), header.getName());
		}

		return headerNames;

	}

	public List<Row> read() {

		if (fileRead.get()) {
			return rows;
		}

		// read file into stream, try-with-resources
		try (Stream<String> stream = new BufferedReader(new InputStreamReader(inputStream)).lines()) {

			Iterator<String> iterator = stream.iterator();

			if (firstLineIsHeader.get() && iterator.hasNext()) {

				List<String> headerNames = readLine(iterator.next());

				for (int i = 0; i < headerNames.size(); i++) {
					String headerName = headerNames.get(i);
					headers.put(headerName, new Header(i, headerNames.get(i)));
				}
			}

			while (iterator.hasNext()) {
				addRow(iterator.next());
			}
			
		}

		fileRead.set(true);

		return rows;
	}

	/**
	 * zero indexed
	 * @param rowNumber
	 * @return the zero indexed row 
	 */
	public Row getRow(int rowNumber){
		return read().get(rowNumber);
	}
	
	public List<Row> getRows() {
		return read();
	}

	public void addRow(String line) {
		List<String> data = readLine(line);

		Row row = new Row();

		rows.add(row);

		for (int i = 0; i < data.size(); i++) {
			String cell = data.get(i);

			if (!Objects.isNull(cell) || !cell.isEmpty()) {
				row.setAt(i, cell);
			}
		}
	}

	private List<String> readLine(String line) {

		final StringTokenizer stringTokenizer = new StringTokenizer(line, DEFAULT_SEPARATOR);

		List<String> data = new ArrayList<>();

		while (stringTokenizer.hasMoreTokens()) {
			data.add(stringTokenizer.nextToken().trim());
		}

		return data;
	}

	public static final class Header {

		private final int index;
		private final String name;

		private Header(int index, String name) {
			this.index = index;
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public String getName() {
			return name;
		}

	}
	
	public static final class Field{
		
		private final String data;
		
		public Field(String data){
			this.data = data;
		}
		
		public String getAsString(){
			return data;
		}
		
		public Double getAsDouble(){
			return Double.valueOf(data);
		}
		
		public Integer getAsInteger(){
			return Integer.valueOf(data);
		}
		
		public boolean isDouble(){
			return Constants.IS_FLOATING_NUMBER_PATTERN.matcher(data).matches();
		}
		
		public boolean isInteger(){
			return Constants.IS_INTEGER_PATTERN.matcher(data).matches();
		}
		
		public boolean isString(){
			return !(isDouble() || isInteger()); 
		}
		
		
		
	}

	public static final class Row {

		private List<Field> data;

		private Row() {
			data = new ArrayList<Field>();
		}

		public List<String> getHeaders() {
			return getHeaders();
		}

		public Field get(String headerName) {
			return getAt(headers.get(headerName).getIndex());
		}

		public void set(String headerName, String data) {
			setAt(headers.get(headerName).getIndex(), data);
		}

		public Field getAt(int index) {
			return data.get(index);
		}

		public void setAt(int index, String data) {
			
			//if the row is empty fill it
			if (this.data.size() < index + 1) {

				int elementsToAdd = (index - this.data.size()) * 2;

				for (int i = 0; i <= elementsToAdd; i++) {
					this.data.add(null);
				}
			}

			this.data.set(index, new Field(data));
		}

		

	}

}

