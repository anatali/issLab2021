#aioconsoleusage.py
import asyncio
import aioconsole


async def echo():
	stdin, stdout = await aioconsole.get_standard_streams()
	async for line in stdin:
		stdout.write(line)

async def work():
	print( "work starts" )
	something = await aioconsole.ainput('>>>') 
	print( something )
	#await asyncio.sleep(1) 
	print( "work ENDS" )

async def main():
	print( "main starts ..." )
	task1 = loop.create_task( work()  )
	await task1
	
loop = asyncio.get_event_loop()	
#print( "starts xx ..." )
#loop.create_task( work()  )

loop.run_until_complete( main() )