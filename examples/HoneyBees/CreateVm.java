package HoneyBees;

import java.util.ArrayList;

import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Vm;

public class CreateVm {
	
	//vmlist creator function
		public ArrayList<Vm> createRequiredVms(int reqVms, int brokerId){
			
			ArrayList<Vm> vmlist = new ArrayList<Vm>();
			
	    	//VM description
			
	    	int vmid = 0;
	    	int mips = 100;		//Chanhe here will change o/p for proposed work.
	    	long size = 1000; //image size (MB)
	    	int ram = 256; //vm memory (MB)
	    	long bw = 70;
	    	int pesNumber = 1; //number of cpus
	    	String vmm = "Xen"; //VMM name

	    	
	    	
	    	for(vmid=0;vmid<reqVms;vmid++){
	    		//add the VMs to the vmList
	    		//int i = mips*(vmid+1);
	    		vmlist.add(new Vm(vmid, brokerId, mips, pesNumber, ram, bw, 
	    				size, vmm, new CloudletSchedulerSpaceShared()));
	    		mips = mips+28;
	    		System.err.println("Vm ID :" +mips);
	    		
	    	}

	    	System.out.println("VmsCreator function Executed... SUCCESS:)");
			return vmlist;

		}
}
