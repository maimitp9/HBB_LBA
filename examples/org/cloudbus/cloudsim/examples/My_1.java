package org.cloudbus.cloudsim.examples;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
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
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;



public class My_1 {
	private static List<Cloudlet> cloudletlist;
	private static List<Vm> vmlist;

public static void main(String []args){
	Log.printLine("First Sample starting....");
	try{
		int num_user=1;
		Calendar cal=Calendar.getInstance();
		boolean tace_flag=false;
		
		CloudSim.init(num_user,cal,tace_flag);
		
		@SuppressWarnings("unused")
		Datacenter datacenter0= createDatacenter("datacenter_0");
		DatacenterBroker broker= createBroker();
		int brokerId=broker.getId();
		
		vmlist= new ArrayList<Vm>();
		
		int vmid=1;
		int mips=1000;
		int size=10000;
		int ram=512;
		int bw=1000;
		int pesnumber=1;
		String vmm="xen";
		
		Vm vm= new Vm(vmid,brokerId,mips,pesnumber,ram,bw,size,vmm, new CloudletSchedulerTimeShared());
		vmlist.add(vm);
		broker.submitVmList(vmlist);
		
		cloudletlist = new ArrayList<Cloudlet>();
		
		int id=0;
		int length=200;
		int filesize=300;
		int outputsize=300;
		UtilizationModel um=new UtilizationModelFull();
		
		Cloudlet cloudlet= new Cloudlet(id, length, pesnumber, filesize, outputsize, um, um, um);
		cloudlet.setUserId(brokerId);
		cloudlet.setVmId(vmid);
		
		cloudletlist.add(cloudlet);
		broker.submitCloudletList(cloudletlist);
		CloudSim.startSimulation();
		CloudSim.stopSimulation();
		
		List<Cloudlet> newlist= broker.getCloudletReceivedList();
		printCloudletList(newlist);
		Log.printLine("Finished....good bye");
	}
	catch(Exception e){
		e.printStackTrace();
		Log.printLine("Error...");
	}
	
	}
private static Datacenter createDatacenter(String name){
	List<Host> hostList= new ArrayList<Host>();
	int mips=1000;
	List<Pe> peList= new ArrayList<Pe>();
	peList.add(new Pe(0,new org.cloudbus.cloudsim.provisioners.PeProvisionerSimple(mips)));
	int hostId = 0;
	int ram = 2048; 
	long storage = 1000000; 
	int bw = 10000;
	
	hostList.add(new Host(hostId, new RamProvisionerSimple(ram), new BwProvisionerSimple(bw), storage, peList, new VmSchedulerSpaceShared(peList)));
	String arc="x86";
	String os="Linux";
	String vmm="Xen";
	double time_zone = 10.0; // time zone this resource located
	double cost = 3.0; // the cost of using processing in this resource
	double costPerMem = 0.05; // the cost of using memory in this resource
	double costPerStorage = 0.001; // the cost of using storage in this
									// resource
	double costPerBw = 0.0; // the cost of using bw in this resource
	LinkedList<Storage> storageList = new LinkedList<Storage>();
	
	DatacenterCharacteristics dc= new DatacenterCharacteristics(arc, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);
	Datacenter datacenter= null;
	try{
		datacenter = new Datacenter(name, dc, new VmAllocationPolicySimple(hostList), storageList, 0);
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return datacenter;
	
	}

private static DatacenterBroker createBroker(){
	DatacenterBroker broker=null;
	try{
		broker= new DatacenterBroker("Broker");
		
	}
	catch(Exception e){
		e.printStackTrace();
		return null;
	}
	return broker;
	
	}
private static void printCloudletList(List<Cloudlet> list){
	int size=list.size();
	Cloudlet cloudlet;
	
	String ind="    ";
	Log.printLine("--------------------------------------OUTPUT -----------------------------------------------");
	Log.printLine("Cloudlet ID" + ind + "STATUS" + ind
			+ "Data center ID" + ind + "VM ID" + ind + "Time" + ind
			+ "Start Time" + ind + "Finish Time");
	
	DecimalFormat dft= new DecimalFormat("###.##");
	
	for(int i=0;i<size;i++){
		cloudlet=list.get(i);
		Log.print(ind+cloudlet.getCloudletId()+ind+ind+ind);
		
		if (cloudlet.getCloudletStatus()==cloudlet.SUCCESS)
		{
			Log.print("SUCCESS");
			
			Log.printLine(ind+ind+cloudlet.getResourceId()+ind+ind+ind+cloudlet.getVmId()+ind+ind+
					dft.format(cloudlet.getActualCPUTime())+ind+ind+dft.format(cloudlet.getExecStartTime())+ind+ind+ind+
					dft.format(cloudlet.getFinishTime()));
		}
	}
	}

}