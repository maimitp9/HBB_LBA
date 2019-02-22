package org.cloudbus.cloudsim;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.lists.VmList;

public class maimitBroker extends DatacenterBroker {

    public maimitBroker(String name) throws Exception {
        super(name);
    }

    @Override
    public void submitCloudlets() {
        int vmIndex = 0;
        int delay = 10;
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
            
            if (vm!=null){
                vm.updateVmProcessing(CloudSim.clock(), null);
                double cloudletLen=0;
                cloudletLen+=  cloudlet.getCloudletLength();
                double currentCPU = vm.getTotalUtilizationOfCpu(CloudSim.clock());
                //TO-DO -> Use currentCPU to your business rules...
                //This will be done after you send each cloudlet
              /*  int id=0;
            	if(cloudlet.getVmId() == 1){
            		System.out.println("Cloudlet: 1234 - VM: " + vm.getId()
                            + " - Current CPU Usage Percent: " + currentCPU*100);
            		
            	}*/
                
                System.out.println("Cloudlet: " + cloudlet.getCloudletId() + " - VM: " + vm.getId()
                        + " - Current CPU Usage Percent: " + currentCPU*100);
            }
            this.pause(delay);
            CloudSim.runClockTick();
        }
    }
}

