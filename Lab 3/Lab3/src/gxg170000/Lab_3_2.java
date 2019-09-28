package gxg170000;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.resultset.ResultsFormat;
import org.apache.jena.tdb.TDBFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Lab_3_2 {
    public static void main(String[] args) {
        org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);

        Dataset dataset = TDBFactory.createDataset("MyDatabases/Dataset1");

        dataset.begin(ReadWrite.WRITE);
        long start = System.currentTimeMillis();
        Model model = dataset.getDefaultModel();
        model.read("Monterey.rdf");
        long end = System.currentTimeMillis();
        float totalTime = (end-start)/1000;
        System.out.println("Load of Monterary.rdf took " + totalTime + " seconds.");
        dataset.commit();
        dataset.close();
        dataset.end();

        dataset.begin(ReadWrite.READ);
        String stringQuery = "prefix em:<http://urn.monterey.org/incidents#>"
                + "prefix org:<http://www.w3.org/2001/vcard-rdf/3.0#> "
                + "prefix point:<http://www.w3.org/2003/01/geo/wgs84_pos#> "
                + "SELECT ?p ?o WHERE { "
                + "{"
                + "em:incident1621 ?p ?o"
                + "}   "
                + "UNION   "
                + "{"
                + "em:incident1621 ?p1 ?o1 .             "
                + "?o1 ?p ?o .  "
                + "FILTER(?o != em:incident1621)"
                + "}   "
                + "UNION   "
                + "{"
                + "em:incident1621 ?p2 ?o2 .  "
                + "?o2 point:Point ?o3 .  "
                + "?o3 ?p ?o ."
                + "}  "
                + "UNION   "
                + "{"
                + "em:incident1621 ?p2 ?o2 .  "
                + "?o2 org:ORG ?o3 . "
                + "?o3 ?p ?o ."
                + "}"
                + "}";


        Query query = QueryFactory.create(stringQuery);
        QueryExecution ex = QueryExecutionFactory.create(query, dataset.getDefaultModel());

        ResultSet rs = ex.execSelect();

        try {

            FileOutputStream out = new FileOutputStream(new File("Lab3_2_gxg170000.xml"));
            ResultSetFormatter.outputAsXML(out, rs);

            FileOutputStream out1 = new FileOutputStream(new File("Lab3_2EC_gxg170000.rdf"));
            ResultSetFormatter.output(out1, rs, ResultsFormat.FMT_RDF_XML);

            FileOutputStream out2 = new FileOutputStream(new File("Lab3_2EC_gxg170000.ttl"));
            ResultSetFormatter.output(out2, rs, ResultsFormat.FMT_RDF_TTL);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        ex.close();


        dataset.close();
        dataset.end();
    }
}
