
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
 
public class myArffReader {
	 myArffReader(){
		 DataSource source;
		 Instances data;
		try {
			source = new DataSource("data.arff");
			data = source.getDataSet();
			if (data.classIndex() == -1)
				   data.setClassIndex(data.numAttributes() - 1);
			System.out.println(data.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	 }
	 
	 
}
