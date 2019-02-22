package org.cloudbus.cloudsim.examples;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;



public class My_2 {

	private static List<Cloudlet> cloudletlist;
	private static List<Vm> vmlist;
	
	public static void main(String []args){
		
		Log.printLine("Simulation starting....");
		
		int num_user=1;
		Calendar cal=Calendar.getInstance();
		boolean trace_flag=false;
		
		CloudSim.init(num_user, cal, trace_flag);
		
		try{
			
			Datacenter datacenter= createdatacenter("datacenter_0");
			
			DatacenterBroker broker= createbroker();
			int brokerId = broker.getId();
			
			vmlist = new ArrayList<Vm>();
			
			int vmId=0;
			int mips=250;
			int size= 10000;
			int ram=512;
			int bw= 1000;
			int pesNumber = 1;
			String vmm= "Xen";
			
			Vm vm1= new Vm(vmId, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
			
			vmId++;
			
			Vm vm2 = new Vm(vmId, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
			
			vmlist.add(vm1);
			vmlist.add(vm2);
			
			broker.submitVmList(vmlist);
			
			cloudletlist = new ArrayList<Cloudlet>();
			
			int cid=0;
			pesNumber=1;
			int length=1000;
			int fileSize= 300;
			int outputSize = 300;
			UtilizationModel utilizationmodel = new UtilizationModelFull();
			
			Cloudlet cl1 = new Cloudlet(cid, length, pesNumber, fileSize, outputSize, utilizationmodel, utilizationmodel, utilizationmodel);
			cl1.setUserId(brokerId);
			
			cid++;
			Cloudlet cl2 = new Cloudlet(cid, length, pesNumber, fileSize, outputSize, utilizationmodel, utilizationmodel, utilizationmodel);
			cl2.setUserId(brokerId);
			
			cid++;
			Cloudlet cl3 = new Cloudlet(cid, length, pesNumber, fileSize, outputSize, utilizationmodel, utilizationmodel, utilizationmodel);
			cl3.setUserId(brokerId);
			
			cid++;
			Cloudlet cl4 = new Cloudlet(cid, length, pesNumber, fileSize, outputSize, utilizationmodel, utilizationmodel, utilizationmodel);
			cl4.setUserId(brokerId);
			
			cid++;
			Cloudlet cl5 = new Cloudlet(cid, length, pesNumber, fileSize, outputSize, utilizationmodel, utilizationmodel, utilizationmodel);
			cl5.setUserId(brokerId);
			
			cid++;
			Cloudlet cl6 = new Cloudlet(cid, length, pesNumber, fileSize, outputSize, utilizationmodel, utilizationmodel, utilizationmodel);
			cl6.setUserId(brokerId);
			
			cloudletlist.add(cl1);
			cloudletlist.add(cl2);
			cloudletlist.add(cl3);
			cloudletlist.add(cl4);
			cloudletlist.add(cl5);
			cloudletlist.add(cl6);
			
			broker.submitCloudletList(cloudletlist);
			
			broker.bindCloudletToVm(cl1.getCloudletId(),vm1.getId());
			broker.bindCloudletToVm(cl3.getCloudletId(),vm1.getId());
			broker.bindCloudletToVm(cl5.getCloudletId(),vm1.getId());
			
			broker.bindCloudletToVm(cl2.getCloudletId(), vm2.getId());
			broker.bindCloudletToVm(cl4.getCloudletId(), vm2.getId());
			broker.bindCloudletToVm(cl6.getCloudletId(), vm2.getId());
			
			CloudSim.startSimulation();
			List<Cloudlet> newList = broker.getCloudletReceivedList();
			CloudSim.stopSimulation();
			
			printCloudletList(newList);
		}
		catch(Exception e){
			
			e.printStackTrace();
			Log.printLine("Error...");
		}
		
	}


	private static Datacenter createdatacenter(String name) {
		// TODO Auto-generated method stub
		
		List<Host> hostList= new ArrayList<Host>();
		List<Pe> pe = new ArrayList<Pe>();
		
		int mips=1000;
		
		pe.add(new Pe(0, new PeProvisionerSimple(mips)));
		
		int hostId=0;
        int ram = 2048; //host memory (MB)
        long storage = 1000000; //host storage
        int bw = 10000;
		
        hostList.add(
        		new Host(hostId, new RamProvisionerSimple(ram), new BwProvisionerSimple(bw), storage, pe, new VmSchedulerTimeShared(pe))
        		);
        
        String arch = "x86";      // system architecture
        String os = "Linux";          // operating system
        String vmm = "Xen";
        double time_zone = 10.0;         // time zone this resource located
        double cost = 3.0;              // the cost of using processing in this resource
        double costPerMem = 0.05;		// the cost of using memory in this resource
        double costPerStorage = 0.001;	// the cost of using storage in this resource
        double costPerBw = 0.0;	
        LinkedList<Storage> newstorage = new LinkedList<Storage>();
        
        DatacenterCharacteristics charectristic = new DatacenterCharacteristics(arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);
        
        Datacenter datacenter = null;
        
        try{
        	
        	datacenter = new Datacenter(name, charectristic, new VmAllocationPolicySimple(hostList), newstorage, 0);
        	
        }
        catch(Exception e){
        	e.printStackTrace();
        }
		return datacenter;
	}


	private static DatacenterBroker createbroker() {
		// TODO Auto-generated method stub
		DatacenterBroker broker = null;
		try{
			
			broker = new DatacenterBroker("Broker");
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return broker;
	}
	
	private static void printCloudletList(List<Cloudlet> newlist) {
		// TODO Auto-generated method stub
		
		int size = newlist.size();
		Cloudlet cloudlet;
		
		 String indent = "    ";
	        Log.printLine();
	        Log.printLine("========== OUTPUT ==========");
	        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
	                "Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time");

	        DecimalFormat dft = new DecimalFormat("###.##");
	        for (int i = 0; i < size; i++) {
	            cloudlet = newlist.get(i);
	            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

	            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
	                Log.print("SUCCESS");

	            	Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
	                     indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent + dft.format(cloudlet.getExecStartTime())+
                          indent + indent + dft.format(cloudlet.getFinishTime()));
	            }
	        }
	}
}
