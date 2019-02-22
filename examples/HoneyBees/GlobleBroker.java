package HoneyBees;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.text.StyledEditorKit.UnderlineAction;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.lists.VmList;



public class GlobleBroker extends DatacenterBroker {
	
	
	
	public  HashMap<Integer,Double> allVmList = new HashMap<Integer,Double>();
	public  HashMap<Integer,Double> UnderloadedList = new HashMap<Integer,Double>();
	public  HashMap<Integer,Double> BalancedList = new HashMap<Integer,Double>();
	public  HashMap<Integer,Double> OverloadedList = new HashMap<Integer,Double>();
	public  Map<Integer, Set<Integer>> mapping = new HashMap<Integer, Set<Integer>>();
	public static int reqTasks ; 
	public static int reqVms ;

	
	public GlobleBroker(String name) throws Exception {
		super(name);
		// TODO Auto-generated constructor stub
		System.err.println("Process is started.");
		reqTasks = cloudletList.size();
		reqVms = vmList.size();
		
	}
	
	 public void submitCloudlets() {
	        int vmIndex = 0;
	        int delay = 10;
	    	VmMonitor utiOfVm = new VmMonitor();
	        for (Cloudlet cloudlet : getCloudletList()) {
	            Vm vm;

	            if (cloudlet.getVmId() == -1) {
	                vm = getVmsCreatedList().get(vmIndex);
	            } else {
	                vm = VmList.getById(getVmsCreatedList(), cloudlet.getVmId());

	                if (vm == null) {
	                    Log.printLine(CloudSim.clock() + ": " + getName() + ": Postponing execution of cloudlet "
	                            + cloudlet.getCloudletId() + ": bount VM not available");
	                    continue;
	                }
	            }

	            Log.printLine(CloudSim.clock() + ": " + getName() + ": Sending cloudlet "
	                    + cloudlet.getCloudletId() + " to VM #" + vm.getId());
	            cloudlet.setVmId(vm.getId());
	            schedule(getVmsToDatacentersMap().get(vm.getId()), delay, CloudSimTags.CLOUDLET_SUBMIT, cloudlet );
	            //sendNow(getVmsToDatacentersMap().get(vm.getId()), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
	            cloudletsSubmitted++;
	            vmIndex = (vmIndex + 1) % getVmsCreatedList().size();
	            getCloudletSubmittedList().add(cloudlet);
	            //Cloudlet was submitted...checking VM Status 
	      //edit      
	            // get utilization after sending each cloudlet to vm
	         /*   if (vm!=null){
	                vm.updateVmProcessing(CloudSim.clock(), null);
	                double cloudletLen=0;
	                cloudletLen+=  cloudlet.getCloudletLength();
	                double currentCPU = vm.getTotalUtilizationOfCpu(CloudSim.clock());
	                DecimalFormat dft = new DecimalFormat("###.##");
	                System.out.println(" Wating Time : " +dft.format(cloudlet.getWaitingTime()));
	            }	*/
	            this.pause(delay);
	            CloudSim.runClockTick();
	        }
	       
	        // take load of individual VMs
	    	/*double[] load = new double[100];
			load = utiOfVm.getVmLoad();
			for(int i=0; i<load.length; i++)
			{
				if(load[i]>0)
				System.out.println("Load on VM :" +load[i]);
			}*/
			
			//Based on load sort VMs
	       
	       allVmList = utiOfVm.getVmUtilization();
	       
	       ArrayList<Entry<Integer, Double>> cop = new ArrayList<>();
			cop.addAll(allVmList.entrySet());
			Collections.sort(cop, new EntryComparator());

			// Display.
			for (Entry<Integer, Double> e : cop) {
			    System.out.println(e.getKey() + "..." + e.getValue());
			}
		    
			// Group VMs and sort by calling VmGrouping and SortVms
			// For UbderLoaded Vm List Sort in Ascending Order
			VmGrouping grouping = new VmGrouping();
			grouping.GroupVm();
			
			
			System.err.println("------------------- UVM Group -------------------");
			UnderloadedList = grouping.getUnderloadedVmList();
			Map<Integer, Double> map = grouping.uvmSort(UnderloadedList);
			Set set2 = map.entrySet();
			Iterator itr = set2.iterator();
			
			System.out.println("VM Id\tVM Load");
			while(itr.hasNext()){
				Map.Entry u = (Map.Entry) itr.next();
				System.out.println(u.getKey() + "\t" + u.getValue());
			}
			/*ArrayList<Entry<Integer , Double>> SortUvm = new ArrayList<>();
			SortUvm.addAll(UnderloadedList.entrySet());
			Collections.sort(SortUvm, new EntryComparator());
			
			//Display Underloaded VMs in Ascending order
			System.out.println("VM Id\tVM Load");
			for(Entry<Integer, Double> u : SortUvm){
				System.out.println(u.getKey() + "\t" + u.getValue());
				
			}*/
			
			//For Balanced Vms
			System.err.println("--------------- Balanced VM Group ---------------");
			BalancedList = grouping.getBalancedVmList();
			Map<Integer, Double> map3 = grouping.BalSort(BalancedList);
			Set set3 = map3.entrySet();
			Iterator itr3 = set3.iterator();
			
			System.out.println("VM Id\tVM Load");
			while(itr3.hasNext()){
				Map.Entry j = (Map.Entry) itr3.next();
				System.out.println(j.getKey() + "\t" + j.getValue());
			}
			/*
			ArrayList<Entry<Integer , Double>> balanced = new ArrayList<>();
			balanced.addAll(BalancedList.entrySet());
			Collections.sort(balanced, new EntryComparator());
			
			//Display Balanced VMs in Ascending order
			System.out.println("VM Id\tVM Load");
			for(Entry<Integer, Double> b : balanced){
				System.out.println(b.getKey() + "\t" + b.getValue());
			}
			*/
						
			//For Over Loaded VMs
			System.err.println("------------------- OVM Group -------------------");
			OverloadedList = grouping.getOverloadedVmList();
			Map<Integer, Double> map4 = grouping.ovmSort(OverloadedList);
			Set set4 = map4.entrySet();
			Iterator itr4 = set4.iterator();
			
			System.out.println("VM Id\tVM Load");
			while(itr4.hasNext()){
				Map.Entry o = (Map.Entry) itr4.next();
				System.out.println(o.getKey() + "\t" + o.getValue());
			}
		/*	ArrayList<Entry<Integer , Double>> SortOvm = new ArrayList<>();
			SortOvm.addAll(OverloadedList.entrySet());
			Collections.sort(SortOvm, new EntryComparator());
			
			//Display Balanced VMs in Descending order
			System.out.println("VM Id\tVM Load");
			for(Entry<Integer, Double> o : SortOvm){
				System.out.println(o.getKey() + "\t" + o.getValue());
			}*/
			
			Vm_Cloudlet_Map csw = new Vm_Cloudlet_Map();
		/*	double ExeTime[] = csw.TransferFromOVM();
			for(int j=0; j<ExeTime.length; j++)
			{
				//Cloudlet cloudlet = getCloudletSubmittedList().get(j);
				if(ExeTime[j] != 0)
				System.out.println("Cloudlet Id :  Time : " +ExeTime[j]);
			} */
			csw.TransferFromOvm();
			csw.TransferToUvm();
			csw.BalVm();
			/*csw.VmWithCloudlet();
			mapping = csw.getSubmittedCloudletToVm();
			Set<Map.Entry<Integer, Set<Integer>>> entrySet = mapping.entrySet();
			Iterator<Map.Entry<Integer, Set<Integer>>> itrOfovm = entrySet.iterator();
			
			while(itrOfovm.hasNext())
			{
				Map.Entry<Integer, Set<Integer>> entry = itrOfovm.next();
				int grp = entry.getKey();
				Set<Integer> SubCloudlets = entry.getValue();
				System.out.println("Vm id# " +grp+ " Submited Cloudlets are : " +SubCloudlets);
			}*/
			
			//csw.bind();
			
			
			//csw.SortCloudlets();
			
			System.err.println("undrloaded List : "+ UnderloadedList.keySet());
			System.err.println("Over List : "+ OverloadedList.keySet());
			System.err.println("Bal List : "+ BalancedList.keySet());
			System.out.println();
			//csw.bind();
			System.out.println();
			
			csw.PrintCloudlets();
			
			grouping.GroupVm();
			System.err.println("undrloaded List : "+ UnderloadedList.keySet());
			System.err.println("Over List : "+ OverloadedList.keySet());
			System.err.println("Bal List : "+ BalancedList.keySet());
			System.out.println();
			
			HashMap<Integer, Double> AllUti = new HashMap<>();
			AllUti = utiOfVm.getVmUtilization();
			
			ArrayList<Entry<Integer, Double>> cop2 = new ArrayList<>();
			cop2.addAll(AllUti.entrySet());
			Collections.sort(cop2, new EntryComparator());

			// Display.
			for (Entry<Integer, Double> e : cop2) {
			    System.out.println(e.getKey() + "..." + e.getValue());
			}
			
			
			
			
		//	double allVmsLoad = utiOfVm.getVmLoad();
		//	System.out.println("Load of All VMs : " +allVmsLoad);
			
		//	double allVmsCapacity = utiOfVm.getCapacityofAllVms();
		//	System.out.println("Capacity of All VMs : " +allVmsCapacity);
			
		//	double ProcessingTime = utiOfVm.getProcessingofAllVms();
		//	System.out.println("Processing Tione of All Vms  : " +ProcessingTime);
			
		//	double StandardDeviation = utiOfVm.StandardDeviation();
		//	System.out.println("Standard Deviation  : " +StandardDeviation);
			
           // ArrayList<Entry<Integer, Double>> sort = utiOfVm.getSortedList(5);
            
            
	        
		    //    	double uti = (jam/vm.getMips());
		      //  	allVmList.put(vm.getId(), uti);
            
		        	
		        	
	        	/*	if (uti<=50)
		            {
		            	underloadedVmList.add(vm.getId());
		            	System.out.println("under--Utlilization of Vm#" +vm.getId()+" --"+uti);
		            }
		            else if(80>=uti)
		            {
		            	balancedVmList.add(vm.getId());
		            	System.out.println("balance-Utlilization of Vm#" +vm.getId()+" --"+uti);
		            }
		            else if(uti>=80)
		            {
		            	overloadedeVmList.add(vm.getId());
		            	System.out.println("over--Utlilization of Vm#" +vm.getId()+" --"+uti);
		            }	
		        	*/
		        	 
		        
		    	
		  /*    Set<Integer> st = allVmList.keySet();
	    		Iterator itr= st.iterator();
	    		
	    		while(itr.hasNext())
	    		{
	    		Integer first= (Integer) itr.next();
	    			System.out.println(first+ " "+allVmList.get(first));
	    			
	    		}	*/
	    		
		     //finish
		        
	    }

	//scheduling function
	
	public void scheduleTaskstoVms(){		
		Cloudlet cloudlet;
		Vm vm;
			
		for(int j=0; j<reqVms; j++)
		{
			vm= getVmList().get(j);
			System.out.println("SUCESS : : : "+vm);
			for(int i=0;i<reqTasks;i++){
				cloudlet = getCloudletSubmittedList().get(i);
				
				if(cloudlet.getVmId() == vm.getId())
				{
					bindCloudletToVm(i, (i%reqVms)); 	//Here is FCFS's magic is done.
					System.out.println("Task " +cloudletList.get(i).getCloudletId()+" is bound with VM"+vmList.get(i%reqVms).getId());
			
				}
			}	
		}
    	System.out.println("\n");
	}
	
	
	public  HashMap<Integer, Double> getAllVmList(){
		return allVmList;
	}

	public static int getReqTasks() {
		return reqTasks;
	}

	public static int getReqVms() {
		return reqVms;
	}

}
class EntryComparator implements Comparator<Entry<Integer, Double>> {

    public int compare(Entry<Integer, Double> arg1, Entry<Integer, Double> arg0) {
	// Compare the values.
	return arg1.getValue().compareTo(arg0.getValue());
    }
}
