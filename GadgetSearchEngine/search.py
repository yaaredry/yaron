import assemble
import string
from itertools import product
from assemble import assemble_data as aData
from assemble import assemble_file as aFile
from ast import literal_eval as make_touple

GENERAL_REGISTERS = [
    'eax', 'ebx', 'ecx', 'edx', 'esi', 'edi'
]


ALL_REGISTERS = GENERAL_REGISTERS + [
    'esp', 'eip', 'ebp'
]

#globalvarCombosList = []
class GadgetSearch(object):

    def __init__(self, dump_path, start_addr):

        """
        Construct the GadgetSearch object.

        Input:
            dump_path: The path to the memory dump file created with GDB.
            start_addr: The starting memory address of this dump.
        """
     
        self.start_address=start_addr
        self.data=open(dump_path).read()


    def get_format_count(self, gadget_format):
        """
        Get how many different register placeholders are in the pattern.
        
        Examples:
            self.get_format_count('POP ebx')
            => 0
            self.get_format_count('POP {0}')
            => 1
            self.get_format_count('XOR {0}, {0}; ADD {0}, {1}')
            => 2
        """
        #   todo : use the string.Formatter().parse:
        #   import string
        
        curMax=0

        for i in string.Formatter().parse(gadget_format):
            for j in i:
                
                if (j is not None and j.isdigit()):
                    
                    if int(j)>=curMax:
                        curMax = int(j)+1
        return (curMax)


    def get_register_combos(self, nregs, registers):
        """
        Return all the combinations of `registers` with `nregs` registers in
        each combination. Duplicates ARE allowed!

        Example:
            self.get_register_combos(2, ('eax', 'ebx','ecx'))
            => [['eax', 'eax'],
                ['eax', 'ebx'],
                ['ebx', 'eax'],
                ['ebx', 'ebx']]

        """
        #print('get_register_combos: nregs: '+str(nregs))
        #print('get_register_combos: registers: '+str(registers))
        #for i in registers:
        #    print i

        comlist=[]
      
        if (len(registers) ==1):
           
            comlist = list(registers)

        elif (nregs ==1):
            for i in registers:
                comlist.append(i)

        else:
            for i in list(product(registers,repeat=nregs)):
                comlist.append(i)
        
        #print "get_register_combos: comlist: " + str(comlist)
        return comlist;

        """ RECURSIVE IMPLEMENTATION - was left here in order to not take python for granted
        :)
        result = []

        for reg in registers:
            result.append([self.func_b(nregs-1,registers,[reg])])

        #print(str(globalvar))
        #print(len(globalvar))
        return globalvarCombosList;

    def func_b(self,nregs,reg_list,cur_list):
        global globalvarCombosList

        if nregs==0:
            globalvarCombosList.append(cur_list)

        else:
            for reg in reg_list:
                new_list = cur_list + [reg]
                self.func_b(nregs-1,reg_list,new_list)
    """

    def format_all_gadgets(self,gadget_format, registers):
        """
        Format all the possible gadgets for this format with the given
        registers.

        Example:
            self.format_all_gadgets("POP {0}; ADD {0}, {1}", ('eax', 'ecx'))
            => [['POP eax; ADD eax, eax'],
                ['POP eax; ADD eax, ecx'],
                ['POP ecx; ADD ecx, eax'],
                ['POP ecx; ADD ecx, ecx']]
        """
        # Hints:
        # 1. Use the format function:
        #    'Hi {0}! I am {1}, you are {0}'.format('Luke', 'Vader')
        #    => 'Hi Luke! I am Vader, you are Luke'
        # 2. You can use an array instead of specifying each argument. Use the
        #    internet, the force is strong with StackOverflow.

        num=self.get_format_count(gadget_format)
        
        #print "Format_all_gadget: num: " + str(num)
        #print "Format_all_gadget: registers: " + str(registers)
        #print "Format_all_gadget: gadjet_format: " +gadget_format

        comboslist=self.get_register_combos(num,registers)
        lst=[]

        #print "Format_all_gadget: comboslist: " + str(comboslist)

        for i in comboslist:
            if (num==1):
                lst.append(gadget_format.format(i))
            else:
            #print "Format_all_gadget: i: " + i
                lst.append(gadget_format.format(*i))

        #print "Format_all_gadget: lst: " + str(lst)
        return lst;



    def find_all(self, gadget):
        """
        Return all the addresses of the gadget inside the memory dump.

        Example:
            self.find_all('POP eax')
            => < all ABSOLUTE addresses in memory of 'POP eax; RET' >
        """
        # Notes:
        # 1. Addresses are ABSOLUTE (for example, 0x08403214), NOT RELATIVE to the
        #    beginning of the file (for example, 12).
        # 2. Don't forget to add the 'RET'
        
        libcfile=self.data
        tempAddr=self.start_address
        
        #print "find_all gadget = " +gadget

        #endAddr=tempAddr+len(libcfile)
        runIndex=0
        result=[]

        opcode = assemble.assemble_data(gadget+'; ret') 

        while runIndex<len(libcfile):
            runIndex=libcfile.find(opcode,runIndex)
            if runIndex==-1:
                break
            address=int(tempAddr+runIndex)
            #result.append('%08x' % address) ##to retrieve string
            result.append(address)
            runIndex=runIndex+len(opcode)

        return result; ##result is a list of long's

    def find(self, gadget, condition=None):
        """
        Return the first result of find_all. If condition is specified, only
        consider addresses that meet the condition.
        """
        condition = condition or (lambda x: True)
        try:
            return next(addr for addr in self.find_all(gadget) if condition(addr))
        except StopIteration:
            raise ValueError("Couldn't find matching address for " + gadget)


    def find_all_formats(self, gadget_format, registers=GENERAL_REGISTERS):
        """
        Similar to find_all - but return all the addresses of all
        possible gadgets that can be created with this format and registers.
        Every elemnt in the result will be a tuple of the gadget string and
        the address in which it appears.

        Example:
            self.find_all_formats('POP {0}; POP {1}')
            => [('POP eax; POP ebx', address1),
                ('POP ecx; POP esi', address2),
                ...]
        """
        lst_of_touples=[]
        tempdata=self.data
        lst=[]
        lst_no_dups=[]
        
        #format_count=self.get_format_count(gadget_format)     
        #regscombos=self.get_register_combos(format_count,GENERAL_REGISTERS)
        #print regscombos
        
        #print "regscombos: " + str(regscombos)

        #for s in regscombos:
            #print "gadget_format: " + gadget_format
            #print "s = " + str(s)
        lst=self.format_all_gadgets(gadget_format,registers)
    

        for i in lst:
            tempresult=self.find_all(i)
            if tempresult:
                for j in tempresult:
                    newtouple=i,j
                    lst_of_touples.append(newtouple)

        #for k in lst_of_touples:
        #    if k not in lst_no_dups:
        #        lst_no_dups.append(k)
        #    print k

        return lst_of_touples;
        

    def find_format(self, gadget_format, registers=GENERAL_REGISTERS, condition=None):
        """
        Return the first result of find_all_formats. If condition is specified,
        only consider addresses that meet the condition.
        """
        condition = condition or (lambda x: True)
        try:
            return next(
                addr for addr in self.find_all_formats(gadget_format, registers)
                if condition(addr))
        except StopIteration:
            raise ValueError(
                "Couldn't find matching address for " + gadget_format)
