package org.cloudbus.cloudsim;

import java.util.List;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.lists.VmList;

public class sortMaimitBroker extends DatacenterBroker  {
	
	public sortMaimitBroker(String name) throws Exception {
        super(name);
    }

    protected void submitCloudlets() {

        //Ordering cloudlets by length
        List<Cloudlet> lstCloudlets = getCloudletList();
        for (int a = 0; a < lstCloudlets.size(); a++) {
            for (int b = a + 1; b < lstCloudlets.size(); b++) {
                if (lstCloudlets.get(b).getCloudletLength() < lstCloudlets.get(a).getCloudletLength()) {
                    Cloudlet temp = lstCloudlets.get(a);
                    lstCloudlets.set(a, lstCloudlets.get(b));
                    lstCloudlets.set(b, temp);
                }
            }
        }

        //Printing ordered list of cloudlets
        for (Cloudlet cl : lstCloudlets) {
            System.out.println("Cloudlet id = " + cl.getCloudletId() + " - Length = " + cl.getCloudletLength());
        }

        //Normal submitCloudlets code
        int vmIndex = 0;
//        int delay =0;
        for (Cloudlet cloudlet : getCloudletList()) {
            Vm vm;
            // if user didn't bind this cloudlet and it has not been executed yet
            if (cloudlet.getVmId() == -1) {
                vm = getVmsCreatedList().get(vmIndex);
            } else { // submit to the specific vm
                vm = VmList.getById(getVmsCreatedList(), cloudlet.getVmId());

                if (vm == null) { // vm was not created
                    Log.printLine(CloudSim.clock() + ": " + getName() + ": Postponing execution of cloudlet "
                            + cloudlet.getCloudletId() + ": bount VM not available");
                    continue;
                }
            }

            Log.printLine(CloudSim.clock() + ": " + getName() + ": Sending cloudlet "
                    + cloudlet.getCloudletId() + " to VM #" + vm.getId());
            cloudlet.setVmId(vm.getId());
//            schedule(getVmsToDatacentersMap().get(vm.getId()),delay ,CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
            sendNow(getVmsToDatacentersMap().get(vm.getId()), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
            cloudletsSubmitted++;
            vmIndex = (vmIndex + 1) % getVmsCreatedList().size();
            getCloudletSubmittedList().add(cloudlet);
//            delay+=10;
            this.pause(0.11);
            CloudSim.runClockTick();
        }

        // remove submitted cloudlets from waiting list
        for (Cloudlet cloudlet : getCloudletSubmittedList()) {
           getCloudletList().remove(cloudlet);
        }

    }



}
