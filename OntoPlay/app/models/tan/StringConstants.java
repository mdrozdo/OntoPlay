package models.tan;

public class StringConstants {
	private static final String ConsumerCountQuery = "Prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> Prefix tan:<http://www.tan.com#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> SELECT (COUNT(?consumer) as ?count) WHERE { SELECT DISTINCT ?consumer WHERE { ?consumer tan:isInterestedIn ?quantifiableInterest; tan:isTargetedBy <%s>.}}";
	private static final String InterestsQuery = "Prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> Prefix tan:<http://www.tan.com#> Prefix xsd: <http://www.w3.org/2001/XMLSchema#> SELECT ?name ?interest ?sum ?avg ?min ?max ?count WHERE { ?interest tan:hasName ?name. {SELECT  ?interest (SUM(?level) as ?sum ) (AVG(?level) as ?avg) (MIN(?level) as ?min) (MAX(?level) as ?max)  (COUNT(?consumer) as ?count) WHERE { ?consumer tan:isInterestedIn ?quantifiableInterest; tan:isTargetedBy <%s>. ?quantifiableInterest tan:hasInterest ?interest; tan:hasLevel ?level. } GROUP BY ?interest ORDER BY DESC (?consumer)}}";
	private static final String PropertyQuery = "Prefix tan:<http://www.tan.com#> Prefix xsd: <http://www.w3.org/2001/XMLSchema#> Prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> Prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT ?name ?value (COUNT(?value) as ?count) WHERE { ?consumer ?property ?value. ?consumer tan:isTargetedBy <%s>. {SELECT DISTINCT ?property ?name WHERE { ?consumer ?property ?propvalue. ?property tan:statistical 'true'^^xsd:boolean; rdfs:label ?name. }}} GROUPBY ?name ?value ORDER BY DESC (?count) ";
	
	
	public static String getInterestsQuery(String targetName)
	{
		return String.format(StringConstants.InterestsQuery, targetName);
	}
	
	public static String getPropertyQuery(String targetName)
	{
		return String.format(StringConstants.PropertyQuery, targetName);
	}
	
	public static String getConsumerCountQuery(String targetName )
	{
		return String.format(StringConstants.ConsumerCountQuery, targetName);
	}
}
