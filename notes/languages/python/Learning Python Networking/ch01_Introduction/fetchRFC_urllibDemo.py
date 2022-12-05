import sys, urllib.request

def getRFCText(rfcNumber):
    url = f'http://www.rfc-editor.org/rfc/rfc{rfcNumber}.txt'      # URL for accessing text for a specified RFC 
    return urllib.request.urlopen(url).read() 


if __name__ == "__main__":
    try: 
        print(getRFCText(int(sys.argv[1])))
    except (IndexError, ValueError):
        print("supply RFC number as command line argument") 
        sys.exit(2)