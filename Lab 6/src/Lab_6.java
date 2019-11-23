import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.OWL;

public class Lab_6 {
    public static void main(String[] args) {
        org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        RDFConnectionRemoteBuilder builder = RDFConnectionFuseki.create().destination("http://localhost:3030/FedSet1/query");

        try {
            RDFConnectionFuseki connection = (RDFConnectionFuseki) builder.build();
            Query query1 = QueryFactory.create("select ?person18 ?person19 ?personName where\r\n" + "{\r\n"
                    + "  ?person18  <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <https://w3id.org/scholarlydata/ontology/conference-ontology.owl#Person>.\r\n"
                    + "  ?person18 <http://xmlns.com/foaf/0.1/mbox_sha1sum> ?mbox.\r\n"
                    + "  ?person18 <http://xmlns.com/foaf/0.1/name> ?personName.\r\n" + "\r\n"
                    + "       SERVICE <http://localhost:3030/FedSet2>\r\n"
                    + "  {?person19 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <https://w3id.org/scholarlydata/ontology/conference-ontology.owl#Person>.\r\n"
                    + "    ?person19 <http://xmlns.com/foaf/0.1/mbox_sha1sum> ?mbox.}\r\n" + "  \r\n" + "\r\n" + "}");

            ResultSet result = ResultSetFactory.copyResults(connection.query(query1).execSelect());
            List<QuerySolution> solutionList1 = ResultSetFormatter.toList(result);
            ResultSetFormatter.out(System.out, result, query1);
            System.out.println("Number of results = " + solutionList1.size());
            System.out.println("End of First Listing");

            Model model = ModelFactory.createDefaultModel();
            List<Resource> listOfIndiviuals = new ArrayList<Resource>();
            for (int i = 0; i < solutionList1.size(); i++) {
                QuerySolution binding = solutionList1.get(i);
                Resource person2018 = (Resource) binding.get("person18");
                Resource person2019 = (Resource) binding.get("person19");
                model.add(person2018, OWL.sameAs, person2019);
                model.add(person2018, DC.creator, "Gnaneswar Gandu");
                if (!listOfIndiviuals.contains(person2018))
                    listOfIndiviuals.add(person2018);
            }
            int count = 0;
            for (int i = 0; i < listOfIndiviuals.size(); i++) {
                String person18 = "<" + listOfIndiviuals.get(0).getNameSpace() + listOfIndiviuals.get(0).getLocalName()+ ">";
                System.out.println(person18);
                String query2 = "select DISTINCT ?personName ?paperName where{" + person18
                        + " <http://www.w3.org/2002/07/owl#sameAs> ?person19.\r\n" + "\r\n"
                        + "  SERVICE <http://localhost:3030/FedSet1>\r\n" + "	{" + person18
                        + "  <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <https://w3id.org/scholarlydata/ontology/conference-ontology.owl#Person>.\r\n"
                        +

                        "    " + person18 + " <http://xmlns.com/foaf/0.1/name> ?personName.}\r\n"
                        + "  SERVICE <http://localhost:3030/FedSet2>\r\n"
                        + "	{?person19 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <https://w3id.org/scholarlydata/ontology/conference-ontology.owl#Person>.\r\n"
                        + "    }\r\n" + "	{  \r\n" + "     SERVICE <http://localhost:3030/FedSet1>\r\n"
                        + "   { ?pprName <http://purl.org/dc/elements/1.1/creator> " + person18 + ".\r\n"
                        + "      ?pprName  <https://w3id.org/scholarlydata/ontology/conference-ontology.owl#title> ?paperName.}}\r\n"
                        + "				union \r\n" + "		{\r\n" + "    SERVICE <http://localhost:3030/FedSet2>\r\n"
                        + "  {     ?person19 <http://xmlns.com/foaf/0.1/made> ?pprName.\r\n" + " \r\n"
                        + "   ?pprName  <https://w3id.org/scholarlydata/ontology/conference-ontology.owl#title> ?paperName.}}\r\n"
                        + "			}  ";
                Query q = QueryFactory.create(query2);
                QueryExecution qe = QueryExecutionFactory.create(q, model);
                result = ResultSetFactory.copyResults(qe.execSelect());
                List<QuerySolution> solutionList2 = ResultSetFormatter.toList(result);
                ResultSetFormatter.out(System.out, result, q);
                count = count + solutionList2.size();

            }
            System.out.println("Number of results=" + count);
            System.out.println("End of Second Listing");

            String query3 = "select DISTINCT ?personName ?paperName where{ ?person18  <http://www.w3.org/2002/07/owl#sameAs> ?person19.\r\n"
                    + "\r\n" + "  SERVICE <http://localhost:3030/FedSet1>\r\n"
                    + "	{?person18  <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <https://w3id.org/scholarlydata/ontology/conference-ontology.owl#Person>.\r\n"
                    +

                    "    ?person18 <http://xmlns.com/foaf/0.1/name> ?personName.}\r\n"
                    + "  SERVICE <http://localhost:3030/FedSet2>\r\n"
                    + "	{?person19 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <https://w3id.org/scholarlydata/ontology/conference-ontology.owl#Person>.\r\n"
                    + "    }\r\n" + "	{  \r\n" + "     SERVICE <http://localhost:3030/FedSet1>\r\n"
                    + "   { ?pprName <http://purl.org/dc/elements/1.1/creator> ?person18 .\r\n"
                    + "      ?pprName  <https://w3id.org/scholarlydata/ontology/conference-ontology.owl#title> ?paperName.}}\r\n"
                    + "				union \r\n" + "		{\r\n" + "    SERVICE <http://localhost:3030/FedSet2>\r\n"
                    + "  {     ?person19 <http://xmlns.com/foaf/0.1/made> ?pprName.\r\n" + " \r\n"
                    + "   ?pprName  <https://w3id.org/scholarlydata/ontology/conference-ontology.owl#title> ?paperName.}}\r\n"
                    + "			}  ";
            Query q = QueryFactory.create(query3);
            QueryExecution qe = QueryExecutionFactory.create(q, model);
            result = ResultSetFactory.copyResults(qe.execSelect());
            List<QuerySolution> solutionList3 = ResultSetFormatter.toList(result);
            ResultSetFormatter.out(System.out, result, q);
            System.out.println("Number of results = " + solutionList3.size());
            System.out.println("End of Third Listing");

            FileOutputStream out = new FileOutputStream(new File("Lab6_gxg170000.n3"));
            model.write(out, "N3");
        }

        catch (Exception e) {
            System.out.println(e);
        }
    }
}
