package ontoplay.models;
/**
 * 
 * @author Motasem Alwazir
 *
 */
public class Constants {

	public final static String DATE_TIME_RANGES = "http://www.w3.org/2001/XMLSchema#dateTime";
	public final static String FLOAT_DATA_RANGES = "http://www.w3.org/2001/XMLSchema#float,"
												+ "http://www.w3.org/2001/XMLSchema#decimal,"
												+ "http://www.w3.org/2001/XMLSchema#double,"
												+ "http://www.w3.org/2001/XMLSchema#boolean";
	
	public final static String INTEGER_DATA_RANGES="http://www.w3.org/2001/XMLSchema#int,"
												+ "http://www.w3.org/2001/XMLSchema#integer";
	
	public final static String STRING_DATA_RANGES="http://www.w3.org/2001/XMLSchema#string."
												+ "http://www.w3.org/2001/XMLSchema#boolean,"
												+ "http://www.w3.org/2001/XMLSchema#anyURI,"
												+ "http://www.w3.org/2000/01/rdf-schema#Literal";
												
	public final static String DATE_MAIN_RANGE="http://www.w3.org/2001/XMLSchema#dateTime";
	public final static String FLOAT_MAIN_RANGE="http://www.w3.org/2001/XMLSchema#float";
	public final static String STRING_MAIN_RANGE="http://www.w3.org/2001/XMLSchema#string";
	public final static String INTEGER_MAIN_RANGE="http://www.w3.org/2001/XMLSchema#integer";
	
	
	/**
	 * Translation to main data property types ranges
	 * @param dataType range URI
	 * @return main range
	 */
	public static final String translateDataType(String dataType){
		if(Constants.DATE_TIME_RANGES.indexOf(dataType)>-1)
			return Constants.DATE_MAIN_RANGE;
		if(Constants.FLOAT_DATA_RANGES.indexOf(dataType)>-1)
			return Constants.FLOAT_MAIN_RANGE;
		if(Constants.STRING_DATA_RANGES.indexOf(dataType)>-1)
			return Constants.STRING_MAIN_RANGE;
		if(Constants.INTEGER_DATA_RANGES.indexOf(dataType)>-1)
			return Constants.INTEGER_MAIN_RANGE;
		return Constants.STRING_MAIN_RANGE;
		
	}
	
			

}
