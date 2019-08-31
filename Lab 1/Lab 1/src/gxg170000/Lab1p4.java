package gxg170000;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.tdb.TDBLoader;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Lab1p4 {
    public static void main(String[] args)
    {
        Logger.getRootLogger().
                setLevel(Level.OFF);

        Dataset dataset= TDBFactory.createDataset("MyDatabases/Dataset1");

        dataset.begin(ReadWrite.WRITE);
        try
        {
            Model model=dataset.getNamedModel("http://gxg170000Friends");
            TDBLoader.loadModel(model, "gxg170000_FOAFFriends.rdf");
            PrintWriter printWriter = null;
            printWriter = new PrintWriter("Lab1p4_gxg170000.xml");
            model.write(printWriter, "RDF/XML");

            printWriter = new PrintWriter("Lab1p4_gxg170000.ntp");
            model.write(printWriter, "N-TRIPLE");

            printWriter = new PrintWriter("Lab1p4_gxg170000.n3");
            model.write(printWriter, "N3");
            dataset.commit();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        finally
        {
            dataset.end();
        }
    }
}
