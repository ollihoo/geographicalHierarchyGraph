# GeographicalHierarchyGraph

This project plays around with geographical data. It uses attraction data that will be saved into
a Neo4j database. With this data it is possible to show a card with all attractions. There are also tools
to calculate distances between pois. 

For legal issues:
This application uses interfaces to OSM to parse data. <a href="http://www.openstreetmap.org/">OpenStreetMap</a> - 
<a href="http://opendatacommons.org/licenses/odbl/">ODbL</a>

It uses also Tour-Pedia for getting attraction data: http://tour-pedia.org/about/


## Installation

### Prerequisites

* JDK 8+
* Virtualbox
* vagrant
* docker
* ansible
    * on Mac: brew install ansible

This project creates a virtualbox by using vagrant and ansible. For a first start:

    cd geoserver/
    vagrant up --provision
    ## wait until the virtualbox is installed

Go into the browser and do:

    http://localhost:10080
    
This shows an initial google map. To import data into database, go to this page:

    http://localhost:10080/create/city/berlin
    http://localhost:10080/create/city/amsterdam

These pages import attraction data for Berlin and Amsterdam. If you intend to play around with the
Neo4j database, get a glance on that URL:

    http://localhost:7474/browser/

To shutdown this server:

    vagrant halt
    
If there is something wrong, get a view on the log files. To see them, do the following steps:

    vagrant ssh
    cd /var/run/app/logs
    less geoserver.log
    
Enjoy this test project.
