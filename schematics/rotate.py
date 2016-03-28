import sys
sys.path.append('/Users/nanne/Desktop/pymclevel')
import mclevel

from os import listdir
from os.path import isfile, join
onlyfiles = [f for f in listdir(".") if isfile(join(".", f)) and f.count('_') == 1]
for f in onlyfiles:
    scheme = mclevel.fromFile(f)
    scheme.saveToFile(f.replace(".", "_0."))
    scheme.rotateLeft()
    scheme.saveToFile(f.replace(".", "_1."))
    scheme.rotateLeft()
    scheme.saveToFile(f.replace(".", "_2."))
    scheme.rotateLeft()
    scheme.saveToFile(f.replace(".", "_3."))
