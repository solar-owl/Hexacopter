from dronekit import connect, VehicleMode, LocationGlobalRelative
import time
import socket
import math
import argparse
import re
import json
##########
print(">>>CONNECTING WITH KOPTER<<<")
def connectMyCopter():
       # parser = argparse.ArgumentParser(description='commands')
        #parser.add_argument('--connect')
        #args = parser.parse_args()
	
       # connection_string = args.connect
        connection_string= "/dev/serial0"
        baud_rate = 57600
        	
        vehicle = connect(connection_string,baud=baud_rate,wait_ready=True)
        print(">>>>CONNECTING IS SUCCESSFUL!!!<<<<")
        time.sleep(5)
        return vehicle
      
#########
def arm():
        while vehicle.is_armable==False:
                data=str(vehicle.attitude)
                regex_num = re.compile('\d.\d+')  
                pitch, yaw, roll = regex_num.findall(data)
                my_json_string = {'pitch': pitch, 'roll': roll, 'yaw': yaw}
                with open('test_file.json', 'w') as file:
                    json.dump(my_json_string, file) 
                print('Attitude: %s'% vehicle.attitude)
                #print(pitch,yaw,roll)
                #print("Waiting for vehicle to become armed..")
                time.sleep(1)
        print("Yo kopter is armed!")
        print("")
	
        vehicle.armed=True
        while vehicle.armed==False:
                print("Waiting for drone to become armed..")
                time.sleep(1)
	
        print("Drone is now armed")
        print("Be careful!")
	
        return None
############
vehicle = connectMyCopter()
arm()
print("End of script.")
	
