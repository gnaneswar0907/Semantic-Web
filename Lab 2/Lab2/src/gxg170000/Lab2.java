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

public class Lab2 {
    public static void main(String[] args) {

        Logger.getRootLogger().
                setLevel(Level.OFF);

        String personURI = "http://utdallas/semclass#person";
        String bookURI = "http://utdallas/semclass#book";
        String radioURI = "http://utdallas/semclass#radio";
        String movieURI = "http://utdallas/semclass#movie";
        String directorURI = "http://utdallas/semclass#director";

        String fullName = "H.G. Wells";

        Dataset dataset = TDBFactory.createDataset("MyDatabases/Dataset1");

        dataset.begin(ReadWrite.WRITE);

        String graphURI = "http://utdallas.edu/semclass/lab2";
        Model model = dataset.getNamedModel(graphURI);

        Resource book = model.createResource(bookURI);
        Resource person = model.createResource(personURI);
        Resource movie = model.createResource(movieURI);
        Resource radioBroadcast = model.createResource(radioURI);
        Resource director = model.createResource(directorURI);

        Resource wells = model.createResource(personURI)
                .addProperty(RDF.type, person)
                .addProperty(VCARD.FN, fullName);

        Resource book1 = model.createResource(bookURI)
                .addProperty(RDF.type, book)
                .addProperty(DC_11.date, "1895")
                .addProperty(DC_11.title, "The Time Machine")
                .addProperty(DC_11.creator,wells);

        Resource book2 = model.createResource(bookURI)
                .addProperty(RDF.type, book)
                .addProperty(DC_11.date, "December 1897")
                .addProperty(DC_11.title, "The War of the Worlds")
                .addProperty(DC_11.creator,wells);

        Resource director1 = model.createResource(directorURI)
                                    .addProperty(RDF.type, director)
                                    .addProperty(VCARD.FN, "Orson Welles");

        Resource director2 = model.createResource(directorURI)
                                    .addProperty(RDF.type, director)
                                    .addProperty(VCARD.FN, "Byron Haskin");

        Resource director3 = model.createResource(directorURI)
                                    .addProperty(RDF.type, director)
                                    .addProperty(VCARD.FN, "Piotr Szulkin");

        Resource director4 = model.createResource(directorURI)
                                    .addProperty(RDF.type, director)
                                    .addProperty(VCARD.FN, "George Pal");

        Resource radio = model.createResource(radioURI)
                                .addProperty(RDF.type, radioBroadcast)
                                .addProperty(DC_11.title, "The War of the Worlds")
                                .addProperty(DC_11.date, "October 30, 1938")
                                .addProperty(DC_11.creator, director1)
                                .addProperty(DC_11.relation, book2);

        Resource movie1 = model.createResource(movieURI)
                                .addProperty(RDF.type, movie)
                                .addProperty(DC_11.title, "The War of the Worlds")
                                .addProperty(DC_11.date, "August 13, 1953")
                                .addProperty(DC_11.creator, director2)
                                .addProperty(DC_11.relation, book2);

        Resource movie2 = model.createResource(movieURI)
                                .addProperty(RDF.type, movie)
                                .addProperty(DC_11.title, "The War of the Worlds: Next Century")
                                .addProperty(DC_11.date, "February 20 1983")
                                .addProperty(DC_11.creator, director3)
                                .addProperty(DC_11.relation, book2);

        Resource movie3 = model.createResource(movieURI)
                                .addProperty(RDF.type, movie)
                                .addProperty(DC_11.title, "The Time Machine")
                                .addProperty(DC_11.date, "July 22, 1960")
                                .addProperty(DC_11.creator, director4)
                                .addProperty(DC_11.relation, book1);

        dataset.commit();
        dataset.end();

        dataset.begin(ReadWrite.READ);

        Model model1 = dataset.getNamedModel(graphURI);

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter("Lab2p3_gxg170000.xml");
            model1.write(printWriter, "RDF/XML");

            printWriter = new PrintWriter("Lab2p3_gxg170000.n3");
            model1.write(printWriter, "N3");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        dataset.end();

    }
}
