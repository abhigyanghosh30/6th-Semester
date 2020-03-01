from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support.expected_conditions import presence_of_element_located
from bs4 import BeautifulSoup

#This example requires Selenium WebDriver 3.13 or newer

f = open('data.txt','r')

for line in f.readlines():
    with webdriver.Firefox() as driver:
        driver.get("http://ltrc.iiit.ac.in/analyzer/hindi/")
        driver.find_element_by_name("input").clear()
        driver.find_element_by_name("input").send_keys(line)
        driver.find_element_by_name("submit").click()
        table = driver.find_elements_by_tag_name('table')[1]
        print()
        soup = BeautifulSoup(table.get_attribute('innerHTML'), 'html.parser')
        for lines in soup.find_all('tr'):
            for data in lines.find_all('td'):
                for content in data.contents:
                    print(content.replace(u'\xa0', u' '),end="")
            print()