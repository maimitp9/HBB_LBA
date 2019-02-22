package HoneyBees;

import java.util.ArrayList;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.UtilizationModelStochastic;

public class CreateCloudlets {
	
	//cloudlet creator
		public ArrayList<Cloudlet> createUserCloudlet(int reqTasks,int brokerId){
			ArrayList<Cloudlet> cloudletList = new ArrayList<Cloudlet>();
			
	    	//Cloudlet properties
	    	int id = 0;
	    	int pesNumber=1;
	    	long length = 1000;
	    	long fileSize = 300;
	    	long outputSize = 300;
	    	UtilizationModel utilizationModel = new UtilizationModelFull();
	    	   	
	    	
	    	for(id=0;id<reqTasks;id++){
	    		
	    		Cloudlet task = new Cloudlet(id, (length*(id+1)), pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
	    		task.setUserId(brokerId);
	    		
	    		
	    		System.out.println("Task "+id+" = "+(task.getCloudletLength()));
	    		
	    		//add the cloudlets to the list
	        	cloudletList.add(task);
	    	}

	    	System.out.println("SUCCESSFULLY Cloudletlist created :)");

			return cloudletList;
			
		}

}
