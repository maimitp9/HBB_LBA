package HoneyBees;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class VmMonitor {
		
		
	double load = 0;	
	
HashMap<Integer, Double> hm;

// return hasMap<vmid, load>

public HashMap<Integer, Double> getVmUtilization(){
	First sc =new First();
	GlobleBroker brok =sc.broker;	
	 hm = new HashMap<Integer, Double>();
	 
		double jam=0;
		int VmSize = brok.getVmList().size();
		int CloudletSize = brok.getCloudletSubmittedList().size();
		
		String indent = " ";
		System.err.println("\n========== Submitted Cloudlets List To VMs ==========");
		Log.printLine("VM ID"+indent+"\tCloudlet ID");
		for (int i = 0; i <VmSize; i++) {
			//double load = 0;
		    Vm vm = brok.getVmList().get(i);
		    String subList = " ";
		    for(int j=0; j<CloudletSize ;j++)
		    {
		    	Cloudlet cloudlet = brok.cloudletSubmittedList.get(j);
		    	if (cloudlet.getVmId() == vm.getId())
		    	{
		    		subList+= indent+ indent+ cloudlet.getCloudletId();
		    		jam+=cloudlet.getCloudletLength();
		    	}
		    	
		    }	
		    System.out.println(indent+ vm.getId()+indent+"\t" + subList + "\t" );
		   	 		  
		}
		System.out.println();
		return hm;
			
	}

// return vmload array
VmGrouping vg = new VmGrouping();

public double[] getVmLoad(){
	
	
  
  First sc =new First();
	GlobleBroker brok =sc.broker;	
	int VmSize = brok.getVmList().size();
	int CloudletSize = brok.getCloudletSubmittedList().size();
	

	double jam=0;
	//double TotLoad = 0;
	double load[] = new double[VmSize];
	//String indent = " ";
	//Log.printLine("========== Submitted Cloudlets List To VMs ==========");
	//Log.printLine("VM ID"+indent+"\tCloudlet ID");
	for (int i = 0; i <VmSize; i++) {
	    Vm vm = brok.getVmList().get(i);
	   // String subList = " ";
	    for(int j=0; j<CloudletSize ;j++)
	    {
	    	Cloudlet cloudlet = brok.cloudletSubmittedList.get(j);
	    	if (cloudlet.getVmId() == vm.getId())
	    	{
	    		//subList+= indent+ indent+ cloudlet.getCloudletId();
	    		jam+=cloudlet.getCloudletLength();
	    	}
	    	
	    }	
	   // System.out.println("\t" + subList + "\t" );
	    load[i] = jam/vm.getMips();
	    //TotLoad+= load[i];  
	    //hm.put(vm.getId(), load);
	 	jam = 0;
	 
	}
	return load;	
	//return TotLoad;
	
}

//return load on perticular Vm

public double getSpecificVmLoad(int CloudletId, int srcVmId, int destVmId){
	
	
	  
	  First sc =new First();
		GlobleBroker brok =sc.broker;	
		int VmSize = brok.getVmList().size();
		int CloudletSize = brok.getCloudletSubmittedList().size();

		double jam=0;
		//double TotLoad = 0;
		double load = 0;
		double ClLength = 0;
		//String indent = " ";
		//Log.printLine("========== Submitted Cloudlets List To VMs ==========");
		//Log.printLine("VM ID"+indent+"\tCloudlet ID");
		for (int i = 0; i <VmSize; i++) {
		    Vm vm = brok.getVmList().get(i);
		   // String subList = " ";
		    if(destVmId == vm.getId()){
		    	for(int j=0; j<CloudletSize ;j++)
			    {
			    	Cloudlet cloudlet = brok.cloudletSubmittedList.get(j);
			    	if (cloudlet.getVmId() == vm.getId())
			    	{
			    		//subList+= indent+ indent+ cloudlet.getCloudletId();
			    		jam+=cloudlet.getCloudletLength();
			    	}
			    	else if(cloudlet.getCloudletId() == CloudletId)
			    	{
			    		 ClLength = cloudlet.getCloudletLength();
			    		System.out.println("\ncloudlet id :" +cloudlet.getCloudletId()+" VM Id : " +destVmId);
			    	}
			    }
		    	 load = (jam+ClLength)/vm.getMips();
		    	 
		    	 Vm vm2 = brok.getVmList().get(destVmId);
		    	 double cap = vm2.CapacitiyofVm();
		    	// System.out.println("Total Capacity : " +cap);
				 double CapPer =Math.round((load*100)/cap);
				// System.out.println("Total Capacity in % : " +CapPer);
				 double ThirtyPer = cap*30/100; 
				 double EightyPer = cap*80/100;
				 
				 double check = 0;
				 if(load < ThirtyPer){
					 
					 //System.err.println("sorce id is :"+brok.OverloadedList.keySet());
					// brok.UnderloadedList.put(srcVmId, CapPer);
					 brok.bindCloudletToVm(CloudletId, destVmId);
					 System.out.println("Underloaded List : "+brok.UnderloadedList.keySet());
					 check = ((CapPer>=1 && CapPer <=30)?CapPer:100);
				//	 System.out.println("Added to Underloaded List VM# " +vm.getId() + " Load is : " +check+ "%");
					 
				 }
				 else if( load < EightyPer){
					 if (brok.UnderloadedList.containsKey(destVmId))
					 {
						 brok.UnderloadedList.remove(destVmId);
					 }
					 double chLoad = getSourceVmLoad(CloudletId, srcVmId, destVmId);
					 
						 brok.OverloadedList.remove(CloudletId);
					 
					 brok.bindCloudletToVm(CloudletId, destVmId);
					 System.out.println("Underloaded List :"+brok.UnderloadedList.keySet());
					 brok.BalancedList.put(destVmId, CapPer);
					 System.err.println("Balanced List : "+brok.BalancedList.keySet());
					 check = ((CapPer>=31 || CapPer <=80)?CapPer:100);
				//	System.out.println("Added to balanced List VM# "+vm.getId() + " Load is : " +check+ "%");
					break;
					 
				 }
				 
					 
		    	 jam = 0;
		    }
		    
		    	
		   // System.out.println("\t" + subList + "\t" );
		   
		    //TotLoad+= load[i];  
		    //hm.put(vm.getId(), load);
		 	 
		}
		return load;	
		//return TotLoad;
		
	}
public double getSourceVmLoad(int CloudletId, int srcVmId, int destVmId){
	
	
	  
	  First sc =new First();
		GlobleBroker brok =sc.broker;	
		int VmSize = brok.getVmList().size();
		int CloudletSize = brok.getCloudletSubmittedList().size();
		
		 Vm vm2 = brok.getVmList().get(srcVmId);
    	 double cap = vm2.CapacitiyofVm();
    	// System.out.println("Total Capacity : " +cap);
		 
		// System.out.println("Total Capacity in % : " +CapPer);
		 double ThirtyPer = cap*30/100; 
		 double EightyPer = cap*80/100;

		double jam=0;
		//double TotLoad = 0;
		double load = 0;
		double ClLength = 0;
		//String indent = " ";
		//Log.printLine("========== Submitted Cloudlets List To VMs ==========");
		//Log.printLine("VM ID"+indent+"\tCloudlet ID");
		for (int i = 0; i <VmSize; i++) {
		    Vm vm = brok.getVmList().get(i);
		   // String subList = " ";
		    if(srcVmId == vm.getId()){
		    	for(int j=0; j<CloudletSize ;j++)
			    {
			    	Cloudlet cloudlet = brok.cloudletSubmittedList.get(j);
			    	if (cloudlet.getVmId() == vm.getId())
			    	{
			    		//subList+= indent+ indent+ cloudlet.getCloudletId();
			    		jam+=cloudlet.getCloudletLength();
			    	}
			    		
			    }
		    	 Cloudlet SelectedCloudlet = brok.getCloudletSubmittedList().get(CloudletId);
	    		 ClLength = SelectedCloudlet.getCloudletLength();
	    		System.out.println("\ncloudlet id :" +SelectedCloudlet.getCloudletId()+" Vm Id : "+srcVmId);
		    	 load = (jam-ClLength)/vm.getMips();
		    	 double CapPer =Math.round((load*100)/cap);
		    	 if (load == 0){
		    		 load = ClLength/vm.getMips();
		    		 CapPer =Math.round((load*100)/cap);
		    	 }
		    	 
		    	
				 
				 double check = 0;
				  if(load>ThirtyPer && load < EightyPer){
					  
					  if(brok.OverloadedList.containsKey(srcVmId))
						 {
							 brok.OverloadedList.remove(srcVmId);
						 }
					  else if(brok.UnderloadedList.containsKey(srcVmId))
						 {
							 brok.UnderloadedList.remove(srcVmId);
						 }
					  System.out.println("overloaded List : "+brok.OverloadedList.keySet());
					 brok.BalancedList.put(srcVmId, CapPer);
					 System.out.println("Balanced List : "+brok.BalancedList.keySet());
					 check = ((CapPer>=ThirtyPer || CapPer <=EightyPer)?CapPer:100);
					System.out.println("Added to balanced List VM# "+vm.getId() + " Load is : " +check+ "%");
					 
				 }
				  else if(load<ThirtyPer){
					  
					  if(brok.OverloadedList.containsKey(srcVmId))
						 {
							 brok.OverloadedList.remove(srcVmId);
						 }
					  System.out.println("overloaded List : "+brok.OverloadedList.keySet());
					 brok.UnderloadedList.put(srcVmId, CapPer);
					 System.out.println("UnderLoded List : "+brok.UnderloadedList.keySet());
					 check = ((CapPer>=ThirtyPer || CapPer <=EightyPer)?CapPer:100);
					System.out.println("Added to UnderLoaded List VM# "+vm.getId() + " Load is : " +check+ "%");
				  }
				  else if(load>EightyPer)
				  {
					  
					  System.out.println("Still this Virtual Machine Overloaded");
				  }
				  
					 
		    	 jam = 0;
		    }
		    
		    	
		   // System.out.println("\t" + subList + "\t" );
		   
		    //TotLoad+= load[i];  
		    //hm.put(vm.getId(), load);
		 	 
		}
		return load;	
		//return TotLoad;
		
	}

// return total capacity of all VMs

public double getCapacityofAllVms(){
	First sc =new First();
	GlobleBroker brok =sc.broker;	
	int VmSize = brok.getVmList().size();
	double TotalCapacity = 0;
	double Capacity[] = new double[100];
	int m = 0; 
	 while(m < VmSize){
		 Vm vm = brok.getVmList().get(m);
		 Capacity[m] = (vm.getNumberOfPes()*vm.getMips()) + vm.getBw();
		 TotalCapacity+= Capacity[m];
		 m++;
	 } 
	return TotalCapacity;
}

}

// return processing time taken by all vms

/*public double getProcessingofAllVms(){
	double ProcessingTime = getVmLoad()/getCapacityofAllVms();
	return ProcessingTime;
}*/



/*public double StandardDeviation(){
	First sc =new First();
	GlobleBroker brok =sc.broker;	
	int VmSize = brok.getVmList().size();
	int CloudletSize = brok.getCloudletSubmittedList().size();

	double Standard[] = new double[100];
	double TotalProcessing = 0;
	double jam = 0;
	int m = 0; 
	 while(m < VmSize){
		 Vm vm = brok.getVmList().get(m);
		 for(int j=0; j<CloudletSize ;j++)
		    {
		    	Cloudlet cloudlet = brok.cloudletSubmittedList.get(j);
		    	if (cloudlet.getVmId() == vm.getId())
		    	{
		    		//subList+= indent+ indent+ cloudlet.getCloudletId();
		    		jam+=cloudlet.getCloudletLength();
		    	}
		    	
		    }	
		 double OneVm = jam / vm.getMips();
		 Standard[m] = Math.pow((OneVm - getProcessingofAllVms()),2);
		 TotalProcessing += Standard[m];
		 jam = 0;
		 System.out.println("Total Processing" + TotalProcessing);
		 m++;
	 } 
	 double sd = Math.sqrt((1/VmSize)*TotalProcessing);
	 System.out.println("SD : " + sd);
	return sd;
}
	



	/*public double getUtilization(double time) {
		// TODO Auto-generated method stub
		int VmSize = brok.getVmList().size();
		int CloudletSize = brok.getCloudletSubmittedList().size();
		
		double jam=0;
		double load = 0;
		String indent = " ";
		
		Log.printLine("========== Submitted Cloudlets List To VMs ==========");
		Log.printLine("VM ID"+indent+"\tCloudlet ID");
		for (int i = 0; i <VmSize; i++) {
		    Vm vm = brok.getVmList().get(i);
		    String subList = " ";
		    for(int j=0; j<CloudletSize ;j++)
		    {
		    	Cloudlet cloudlet = brok.cloudletSubmittedList.get(j);
		    	if (cloudlet.getVmId() == vm.getId())
		    	{
		    		subList+= indent+ indent+ cloudlet.getCloudletId();
		    		jam+=cloudlet.getCloudletLength();
		    	}
		    	
		    }	
		    System.out.println("\t" + subList + "\t" );
		    load = jam/vm.getMips(); 
		 		    jam = 0;
		 return load;
		}
		
		
		return load;

	}
	
	

*/
 

   	
   	
   	



