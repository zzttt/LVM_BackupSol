#!/usr/bin/python
#-*- coding: utf-8 -*-

from django.shortcuts import render
from django.template import RequestContext
from django.http import HttpResponse
from django.shortcuts import render_to_response
from priodict import priorityDictionary
from dijkstra import Dijkstra
from dijkstra import shortestPath
from getPath import getMyPath
from models import rtable
from models import lineInfo
# Create your views here.i

G = {
                '0':{'1':200},
                '1': {'2': 344 , '13':535, '14':390,'16': 413},
                '2': {'1': 344 ,'3': 198, '4':221, '14':203},
                '3': {'2': 198},
                '4':{'2':221,'5':190, '6':240, '18': 338},
                '5':{'4':190,'6':113},
                '6':{'4':240, '5':113, '7':133},
                '7':{'6':133,'8':192 , '18': 378},
                '8':{'7':192,'9':274, '18': 373},
                '9':{'8':274,'10':306, '11':214, '18': 258},
                '10':{'9':306,'11':151},
                '11':{'9':214,'10':151, '12':236, '15': 354},
                '12':{'11':236,'13':170, '15': 200, '17': 313,},
                '13':{'1':535 , '12':170},
                '14':{'1':390, '2':203, '16': 180, '18': 281},
                '15':{'11':354, '12':200},
                '16':{'1':413, '14': 180, '18': 186},
                '17':{'12': 313},
                '18':{'4':338, '7':378, '8':373, '9': 258, '14':281, '16':186},
                }

rtableData = rtable.objects.all()


def page1(request):
	rtableData = rtable.objects.all()
# 	print str(rtableData)
	itemList = []
	recomItemList = []
	for item in rtableData:
		itemString = str(item)
		tempData = itemString.split(',')
		itemList.append(tempData)

	itemList = rtableData
	print rtableData

	return render_to_response('main_1.html', {'itemList':itemList})

def requestTest(request): # get방식 데이터받아서 결과계산
#	print request.GET['test']
	myList = request.GET['test']
	# myList 는 rid가 들어옴. rid 를 이용하여 nodeNum을 계산해야.
	
	myList = myList.encode("utf-8")	


#	print myList

	tmpList = myList.split(",")

#	print int(tmpList[0].replace("'","")) #  int 로 변경

	myListForCalc = ""

	for i in tmpList:
		tmpObj = rtable.objects.get(rid = int(i.replace("'","")))
		myListForCalc += "'"+str(tmpObj.nodenum)+"',"
		print tmpObj.nodenum
	
#	print	"tt : ",myListForCalc
	myListForCalc = myListForCalc[:len(myListForCalc)-1]
#	print "ee : ",myListForCalc

	pathData = lineInfo.objects.all()

	tmpG = {}
	tmpSubG = {}
	srcIdx = 0	
	for lineItem in pathData: #Graph를 생성
		if srcIdx == lineItem.src:
			tmpSubG[str(lineItem.dest)] = lineItem.dist
		else:
			tmpG[str(srcIdx)] = tmpSubG
			tmpSubG = {}
			tmpSubG[str(lineItem.dest)] = lineItem.dist
			srcIdx += 1
		#print lineItem.src,lineItem.dest,lineItem.dist

	tmpG[str(srcIdx)] = tmpSubG
#	print "nodeList :" , myListForCalc

#	print "result >>",tmpG
#	print "------------------------"
#	print G
	
	# G 데이터를 기반으로  최적경로를 구한다.
 	result = getMyPath(myListForCalc, tmpG)
#	print result
	#return HttpResponse(request.GET['test'])
	#return HttpResponse(result)
	return render_to_response('main_1.html',{'itemList':rtableData,'result':result,'selectedList':myList})

def main(request): # get방식을 통해 체크항목을 읽어온다.
	# example, CLR p.528
	# makes a testing Graphes (should inquire from sqlite)

	# DB 에서 G 를 읽고 , request.GET을 통해서 선택된 리스트를 읽는다


	#print Dijkstra(G,'1')
	# how we calculate when we want to go like 1,4,6,17
	print shortestPath(G,'1','18')
	
	return render_to_response('test.html')
	#return HttpResponse('asddd')

