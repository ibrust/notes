from urllib.request import urlopen 
from urllib.error import HTTPError 
from urllib.error import URLError
from bs4 import BeautifulSoup 

def fetchHTML(url):
    try: 
        html = urlopen(url)
        return html
    except HTTPError as e:
        print("error opening URL:", e)
        return
    except URLError as e:
        print("server not found:", e)
        return

def getTag(html, tag):
    soup_object = BeautifulSoup(html.read(), features="html.parser")
    if hasattr(soup_object, tag):
        return getattr(soup_object, tag)
    else:
        return None

if __name__ == "__main__":
    html = fetchHTML("http://www.pythonscraping.com/pages/page1.html")
    h1 = getTag(html, "h1")
    print(h1)