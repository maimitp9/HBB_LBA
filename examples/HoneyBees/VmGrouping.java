package HoneyBees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.examples.CloudSimExample8.GlobalBroker;

public class VmGrouping {
	
	public VmGrouping(){
		overloadedeVmList = new HashMap<Integer,Double>();
		underloadedVmList = new HashMap<Integer,Double>();
		balancedVmList = new HashMap<Integer,Double>();
	}
	
	public  HashMap<Integer, Double> overloadedeVmList;
	public  HashMap<Integer, Double> underloadedVmList;
	public  HashMap<Integer, Double> balancedVmList;

	
	public void GroupVm(){
		First sc =new First();
		GlobleBroker brok =sc.broker;	
		int VmSize = brok.getVmList().size();
		
		
		double[] load = new double[100];
		VmMonitor UtiofVm = new VmMonitor();
		load = UtiofVm.getVmLoad();
		
		for (int m = 0; m <VmSize; m++) {
		    Vm vm = brok.getVmList().get(m);
		    double cap = vm.CapacitiyofVm();
		    double CapPer =Math.round((load[m]*100)/cap);
		    double ThirtyPer = cap*30/100; 
		    double EightyPer = cap*80/100;
		    //System.out.println("Thirty Per of Vm : " +ThirtyPer);
		    //System.out.println("Eifhty Per of Vm is : " + EightyPer);
		    
		    
			double check = 0;
				if(load[m] >0){
					if(load[m] < ThirtyPer)
					{
						underloadedVmList.put(vm.getId(), CapPer);
						check = ((CapPer>=1 && CapPer <=30)?CapPer:100);
						System.out.println("Added to Underloaded List VM# " +vm.getId() + " Load is : " +check+ "%");
						//System.out.println(vm.CapacitiyofVm());
					}
					else if( load[m] < EightyPer)
					{
						
						balancedVmList.put(vm.getId(), CapPer);
						check = ((CapPer>=31 || CapPer <=80)?CapPer:100);
						System.out.println("Added to balanced List VM# "+vm.getId() + " Load is : " +check+ "%");
						//System.out.println(vm.CapacitiyofVm());
					}
					else if(load[m]>=EightyPer)
					{
						
						overloadedeVmList.put(vm.getId(), CapPer);
						check = ((CapPer>=81 && CapPer <=100)?CapPer:100);
						System.out.println("Added to Overloaded List VM# "+vm.getId() + " Load is : " +check+ "%" );
						//System.out.println(vm.CapacitiyofVm());
					}
				} 
				
		}
		System.out.println();
		
	}
	
	public  HashMap<Integer, Double> getOverloadedVmList() {
		return overloadedeVmList;
	}

	public  HashMap<Integer, Double> getUnderloadedVmList() {
		return underloadedVmList;
	}

	public  HashMap<Integer, Double> getBalancedVmList() {
		return balancedVmList;
	}
	
	public HashMap uvmSort(HashMap map){
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new EntryComparator());
		
		HashMap SortedhasMap = new LinkedHashMap();
		for(Iterator itr = list.iterator(); itr.hasNext();)
		{
			Map.Entry entry = (Map.Entry) itr.next();
			SortedhasMap.put(entry.getKey(), entry.getValue());
		}
		return SortedhasMap;
	}
	
	public HashMap ovmSort(HashMap ovmMap){
		List ovmList = new LinkedList(ovmMap.entrySet());
		Collections.reverseOrder(new EntryComparator());
		
		HashMap ovmSorted = new LinkedHashMap();
		for(Iterator ovmItr = ovmList.iterator(); ovmItr.hasNext();)
		{
			Map.Entry entry2 = (Map.Entry) ovmItr.next();
			ovmSorted.put(entry2.getKey(),entry2.getValue());
		}
		return ovmSorted;
	}
	
	public HashMap BalSort(HashMap BalMap){
		List BalList = new LinkedList(BalMap.entrySet());
		Collections.sort(BalList, new EntryComparator());

		HashMap BalSortedhasMap = new LinkedHashMap();
		for(Iterator BalItr = BalList.iterator(); BalItr.hasNext();)
		{
			Map.Entry entry3 = (Map.Entry) BalItr.next();
			BalSortedhasMap.put(entry3.getKey(), entry3.getValue());
		}
		return BalSortedhasMap;
	}
	}

