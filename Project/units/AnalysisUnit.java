package units;

import java.util.Vector;

import basicas.Package;
import basicas.Routine;
import drivers.DBMSDriver;
public class AnalysisUnit {

	private DBMSDriver driver;
	
	public AnalysisUnit(DBMSDriver d){
		this.driver = d;
	}
	
	public Package generateRoutinesDependencies(Package p){
		Package ret = p;
		
		for(int i = 0; i < ret.getRoutines().length; i++){
			Routine r = ret.getRoutines()[i];
			
			//check this calls
			Vector<Routine> rCallFor = new Vector<Routine>();
			String rSource = ret.getRoutines()[i].getSource().toUpperCase();
			for(int j = 0; j < ret.getRoutines().length; j++){
				if(ret.getRoutines()[j].getName().equalsIgnoreCase(ret.getRoutines()[i].getName())){
					continue;
				}
				int index = rSource.indexOf(ret.getRoutines()[j].getName());
				if(index != -1){
					rCallFor.add(ret.getRoutines()[j]);
				}
			}
			
			Routine[] rCf = new Routine[rCallFor.size()];
			for(int h = 0; h < rCf.length; h++){
				rCf[h] = rCallFor.elementAt(h);
			}
			
			/*Routine[] rCb = new Routine[rCalledBy.size()];
			for(int h = 0; h < rCb.length; h++){
				rCb[h] = rCalledBy.elementAt(h);
			}*/
			
			
			//r.setCalledBy(rCb);
			r.setCallFor(rCf);
		}
		return ret;
	}
	
	
	}
