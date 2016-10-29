# myRetail Product Viewer

## **Introduction:**
This is a sample case study. The goal for this exercise is to create an end-to-end Proof-of-Concept for a products API, which will aggregate product data from multiple sources and return it as JSON to the caller. 

## **Functionality Covered:**
All 4 operations i.e. GET(by product as well as bulk), POST, PUT and DELETE.

## **Tech Stack:**
* Front End: **Angular Js**
* Back End Programming Language: **Java**
* Web App Framework: **Spring Boot**
* Database: **ElasticSearch NoSQL** Data Store (Open Source)

## **Instructions:**

### **Build Steps and Dependencies:**

1. You will need to have ElasticSearch installed, with Cluster name "**elasticsearch**" and Index named "**myelasticdb**" pre-existing. If you want to use your ElasticSearch setup, modify **ElasticSearch.java** to define the constants suiting your environment.

2. Download the source code in your IDE.

3. Run App.java.

### **Application Interaction:**


1. Open your browser and naviage to http://localhost:8080/index.html
2. On the main search page, either search for a specific Product ID, or, leave the Product ID blank, to get all the products setup in the NOSQL Data Store.
3. Product List page: You can do the following.

  a. Click on the Product ID hyperlink, to navigate to the Product Detail page for that Product ID.

  b. Edit the Price in the Data Store, using the "Commit" button.

  c. Delete the product from the Data Store.

  d. Search for keywords to shortlist the entries in the table.

  e. Sort by Product ID or Product Name.
  
  f. Add new Product and Pricing to the NOSQL Data Store.
  
4. Product Detail Page: 

  a. View Product Informaiton.
  
  b. Edit the in the Data Store, using the price value hyperlink.
  
  c. Navigate to the Datapage Link, using the Data Page URL. 






