package HoneyBees;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.sound.midi.SysexMessage;

import org.apache.commons.math3.genetics.StoppingCondition;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.Vm;

import sun.rmi.transport.proxy.CGIHandler;

import com.sun.imageio.plugins.common.LZWStringTable;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

public class Vm_Cloudlet_Map {
	
	
	
	public Vm_Cloudlet_Map(){
		map = new HashMap<Integer, Set<Integer>>();
		lst = new ArrayList<Cloudlet>();
		
	}
	public static Map<Integer, Set<Integer>>  map;
	List<Cloudlet> lst;
	
	First fc = new First();
	VmGrouping vg = new VmGrouping();
	VmMonitor vmm = new VmMonitor();
	
	
	GlobleBroker brok = fc.broker;
	int VmSize = brok.getVmList().size();
	int CloudletSize = brok.getCloudletSubmittedList().size();
	int ovmSize = brok.OverloadedList.size();
	DecimalFormat dft = new DecimalFormat("###.##");
	
	
	public double[] TransferFromOvm(){
		double[] ovmExeTime = new double[100];
		HashMap<Integer, Double> ovm = brok.OverloadedList;
		HashMap<Integer, Double> sortOvm = new HashMap<Integer, Double>();
		sortOvm = vg.ovmSort(ovm);
		Set<Integer> oo = sortOvm.keySet();
		Iterator itrOvm = oo.iterator();
		
		while(itrOvm.hasNext())
		{
			Integer strOvm = (Integer) itrOvm.next();
			System.err.println("-------------- OVM Group VM Id : "+strOvm+" --------------\n");
			System.out.println("Cloudlet Id \t Expected Exicution Time");
		
				Vm vm = brok.getVmList().get(strOvm);
				
				if(strOvm == vm.getId())
				{
					 double cap = vm.CapacitiyofVm();  
					 double ThirtyPer = cap*30/100; 
					 double EightyPer = cap*80/100;
				
						double jam = 0;
						for(int j=0; j<CloudletSize; j++)
						{
							
							Cloudlet cloudlet = brok.getCloudletSubmittedList().get(j);
							if(cloudlet.getVmId() == vm.getId())
							{
								double vmload =0 ;
								ovmExeTime[j] = cloudlet.getCloudletLength()/vm.getMips();
								System.out.println(cloudlet.getCloudletId()+ "\t\t\t" +dft.format(ovmExeTime[j]));
								
								jam+=cloudlet.getCloudletLength();
								//System.out.println("Sum :"+jam );
								vmload = jam/vm.getMips();
								double CapPer =Math.round((vmload*100)/cap);
								if(vmload<=ThirtyPer)
								{
									//System.out.println("Cloudlet Id UVM :"+cloudlet.getCloudletId()+ " Load : "+CapPer );
								}
								else if(vmload<EightyPer)
								{
									//System.out.println("Cloudlet Id BAL :"+cloudlet.getCloudletId()+ " Load : "+CapPer );
								}
								else
								{
									//System.out.println("Cloudlet Id OVM :"+cloudlet.getCloudletId()+ " Load : "+CapPer );
									//brok.getCloudletSubmittedList().remove(cloudlet.getCloudletId());
									lst.add(cloudlet);
								}
							
							}
						}
					}	
		
			System.out.println();
		}
			
		return ovmExeTime;
	}
	
	public double[] TransferToUvm(){
		double[] uvmExeTime = new double[100];
		HashMap<Integer, Double> uvm = brok.UnderloadedList;
		Set<Integer> uu = uvm.keySet();
		Iterator itrUvm = uu.iterator();
		
		while(itrUvm.hasNext())
		{
			Integer strUvm = (Integer) itrUvm.next();
			System.err.println("-------------- UVM Group VM Id : "+strUvm+" --------------\n");
			System.out.println("Cloudlet Id \t Expected Exicution Time  ");
			
				Vm vm = brok.getVmList().get(strUvm);
				if(strUvm == vm.getId())
				{
					for(int j=0; j<CloudletSize; j++)
					{
						Cloudlet cloudlet = brok.getCloudletSubmittedList().get(j);
						if(cloudlet.getVmId() == vm.getId())
						{
							uvmExeTime[j] = cloudlet.getCloudletLength()/vm.getMips();
							System.out.println(cloudlet.getCloudletId()+ "\t\t\t" +dft.format(uvmExeTime[j]));
						}
					}
				}
			System.out.println();
		}
		return uvmExeTime;
	}
	
	public double[] BalVm(){
		double[] balExeTime = new double[100];
		HashMap<Integer, Double> balvm = brok.BalancedList;
		Set<Integer> bb = balvm.keySet();
		Iterator itrBal = bb.iterator();
		
		while(itrBal.hasNext())
		{
			Integer strBal = (Integer) itrBal.next();
			System.err.println("------------ Balanced Group VM Id : "+strBal+" ------------\n");
			System.out.println("Cloudlet Id \t Expected Exicution Time  ");
			
				Vm vm = brok.getVmList().get(strBal);
				if(strBal == vm.getId())
				{
				//	double cap = vm.CapacitiyofVm();  
					// double sixPer = cap*60/100; 
					 //double EightyPer = cap*80/100;
					 
					 //double jam = 0;
					for(int j=0; j<CloudletSize; j++)
					{
						Cloudlet cloudlet = brok.getCloudletSubmittedList().get(j);
						if(cloudlet.getVmId() == vm.getId())
						{
						//	double vmload = 0;
							balExeTime[j] = cloudlet.getCloudletLength()/vm.getMips();
							System.out.println(cloudlet.getCloudletId()+ " \t\t\t " +dft.format(balExeTime[j]));
							//jam+=cloudlet.getCloudletLength();
							//System.out.println("Sum :"+jam );
							//vmload = jam/vm.getMips();
							//double CapPer =Math.round((vmload*100)/cap);
							/*if(vmload>sixPer)
							{
								//System.out.println("Cloudlet Id UVM :"+cloudlet.getCloudletId()+ " Load : "+CapPer );
								lst.add(cloudlet);
							}
							else if(vmload<EightyPer)
							{
								//System.out.println("Cloudlet Id BAL :"+cloudlet.getCloudletId()+ " Load : "+CapPer );
							}
							else
							{
								//System.out.println("Cloudlet Id OVM :"+cloudlet.getCloudletId()+ " Load : "+CapPer );
								//brok.getCloudletSubmittedList().remove(cloudlet.getCloudletId());
								lst.add(cloudlet);
							}*/
						}
					}
				}
			System.out.println();
			
		}
		return balExeTime;
	}
	
	public Map<Integer, Set<Integer>> VmWithCloudlet(){
		double[] ovmExeTime = new double[100];
		HashMap<Integer, Double> ovm = brok.OverloadedList;
		HashMap<Integer, Double> sortOvm = new HashMap<Integer, Double>();
		sortOvm = vg.ovmSort(ovm);
		Set<Integer> oo = sortOvm.keySet();
		Iterator itrOvm = oo.iterator();
		double lld = 0;
		
		while(itrOvm.hasNext())
		{
			Integer strOvm = (Integer) itrOvm.next();
			//System.err.println("-------------- OVM Group VM Id : "+strOvm+" --------------\n");
			//System.out.println("Cloudlet Id \t Expected Exicution Time");
		
				Vm vm = brok.getVmList().get(strOvm);
				
				if(strOvm == vm.getId())
				{
					int[] OvmGroup = new int[VmSize];
					for(int g=0; g<OvmGroup.length; g++)
					{
						OvmGroup[g] = strOvm;
						Set<Integer> CloudletOvm = new TreeSet<Integer>();
						for(int j=0; j<CloudletSize; j++)
						{
							Cloudlet cloudlet = brok.getCloudletSubmittedList().get(j);
							if(cloudlet.getVmId() == vm.getId())
							{
								
								CloudletOvm.add(cloudlet.getCloudletId());
								//ovmExeTime[j] = cloudlet.getCloudletLength()/vm.getMips();
								//System.out.println(cloudlet.getCloudletId()+ "\t\t\t" +dft.format(ovmExeTime[j]));	
							}	
						}
						map.put(OvmGroup[g], CloudletOvm);
					}	
				}
		}
		
		
		return map;
	}
	
public Map<Integer, Set<Integer>> getSubmittedCloudletToVm(){
	return map;
}
/* public void bind(){
	//brok.bindCloudletToVm(16, 7);
	//TO DO first check all 
	
	double srcVmLoad = vmm.getSourceVmLoad(16,0,7);
	
	//System.out.println("Load on Vm id : "+vm.getId()+" load : "  +srcVmLoad);
	double DestVmLoad = vmm.getSpecificVmLoad(16,0,7);
	//System.out.println("Load on Vm id : " +strUvm);
	//brok.bindCloudletToVm(17, 5);
	double src1VmLoad = vmm.getSourceVmLoad(17,1,5);
	double Dest1VmLoad = vmm.getSpecificVmLoad(17,1,5);
	//brok.bindCloudletToVm(18, 6);
	double src2VmLoad = vmm.getSourceVmLoad(18,2,6);
	double Dest2VmLoad = vmm.getSpecificVmLoad(18,2,6);
	
	
	for(int t=0; t<VmSize; t++){
		Vm vm2 = brok.getVmList().get(t);
		for(int c=0; c<CloudletSize;c++ ){
			Cloudlet clt = brok.getCloudletSubmittedList().get(c);
			
			if(clt.getVmId() == vm2.getId()){
				System.out.println("Vm Id : "+vm2.getId()+" Cloudlet Id : "+clt.getCloudletId());
			}
		}
	}
} */

public void PrintCloudlets()
{
	HashMap<Integer, Double> UvmGrp = brok.UnderloadedList;
	HashMap<Integer, Double> SortedUvm = new HashMap<>();
	SortedUvm = vg.uvmSort(UvmGrp);
	Set uu = SortedUvm.keySet();
	Iterator uuItr = uu.iterator();
	
	
	for (int a = 0; a < lst.size(); a++) {
        for (int b = a + 1; b <lst.size(); b++) {
            if (lst.get(b).getCloudletLength() > lst.get(a).getCloudletLength()) {
                Cloudlet temp = lst.get(a);
                lst.set(a, lst.get(b));
                lst.set(b, temp);
            }
        }
	}
	
	
			for(int i=0; i<lst.size(); i++)
			{
				Cloudlet cl = lst.get(i);
				//double ab = cl.getCloudletLength()-200;
				while(uuItr.hasNext())
				{
					
					Integer strUvm = (Integer) uuItr.next();
					Vm vm = brok.getVmList().get(strUvm);
					if(strUvm == vm.getId())
					{
						//lst.remove(cl);
						double srcVmLoad = vmm.getSourceVmLoad(cl.getCloudletId(), cl.getVmId(), strUvm);
				
						double DestVmLoad = vmm.getSpecificVmLoad(cl.getCloudletId(), cl.getVmId(), strUvm);
						break;
					}
				}
			}	
}
	
	
/*	public double[] SortCloudlets()
	{
		double ExeTime[] = TransferFromOvm();
		for(int j=0; j<ExeTime.length; j++)
		{
			//Cloudlet cloudlet = getCloudletSubmittedList().get(j);
			if(ExeTime[j] != 0)
			{
				for(int k=j+1; k<ExeTime.length; k++ )
				{
					if(ExeTime[k] != 0)
					{
						if(ExeTime[k]>ExeTime[j])
						{
							double temp = ExeTime[j];
							ExeTime[j]= ExeTime[k];
							ExeTime[k] = temp;
							
						}
					}
				}
			}
				
		}
		for(int t=0; t<ExeTime.length; t++)
		{
			if(ExeTime[t] !=0)
			System.out.println("Cloudlet Id :"+t+ " Time : " +ExeTime[t]);
		}
		return ExeTime;
	}*/
	
}

