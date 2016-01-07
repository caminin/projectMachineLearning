
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class myArffReader {
	Instances data;
	public myArffReader(String filename){
		DataSource source;
		try {
			source = new DataSource(filename);
			data = source.getDataSet();
			if (data.classIndex() == -1)
				data.setClassIndex(data.numAttributes() - 1);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	public Instances getData(){
		return data;
	}
}
