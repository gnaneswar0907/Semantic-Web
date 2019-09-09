package gxg170000;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Lab2p2 {
    public static void main(String[] args) {

        Logger.getRootLogger().
                setLevel(Level.OFF);

        String personURI = "http://utdallas/semclass#person";
        String bookURI = "http://utdallas/semclass#book";

        String fullName = "H.G. Wells";

        Dataset dataset = TDBFactory.createDataset("MyDatabases/Dataset1");

        dataset.begin(ReadWrite.WRITE);

        String graphURI = "http://utdallas.edu/semclass/lab2p2";
        Model model = dataset.getNamedModel(graphURI);

        Resource book = model.createResource(bookURI);
        Resource person = model.createResource(personURI);

        Resource wells = model.createResource(personURI)
                                .addProperty(RDF.type, person)
                                .addProperty(VCARD.FN, fullName);

        Resource book1 = model.createResource(bookURI)
                                .addProperty(RDF.type, book)
                                .addProperty(DC_11.date, "01/01/1895")
                                .addProperty(DC_11.title, "The Time Machine")
                                .addProperty(DC_11.creator,wells);

        Resource book2 = model.createResource(bookURI)
                                .addProperty(RDF.type, book)
                                .addProperty(DC_11.date, "12/01/1897")
                                .addProperty(DC_11.title, "The War of the Worlds")
                                .addProperty(DC_11.creator,wells);

        dataset.commit();
        dataset.end();

        dataset.begin(ReadWrite.READ);

        Model model1 = dataset.getNamedModel(graphURI);

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter("Lab2p2_gxg170000.xml");
            model1.write(printWriter, "RDF/XML");

            printWriter = new PrintWriter("Lab2p2_gxg170000.n3");
            model1.write(printWriter, "N3");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        dataset.end();

    }
}
