package com.cosmiiko.bsodod;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface NtDll extends StdCallLibrary {
    NtDll INSTANCE = Native.loadLibrary("NtDll", NtDll.class, W32APIOptions.DEFAULT_OPTIONS);

    // See http://www.pinvoke.net/default.aspx/ntdll.RtlAdjustPrivilege
    public long RtlAdjustPrivilege(long Privilege, boolean bEnablePrivilege, boolean IsThreadPrivilege, Pointer PreviousValue);

    // See http://undocumented.ntinternals.net/index.html?page=UserMode%2FUndocumented%20Functions%2FError%2FNtRaiseHardError.html
    public long NtRaiseHardError(long ErrorStatus, long NumberOfParameters, long UnicodeStringParameterMask, int Parameters, long ValidResponseOption, Pointer Response);
}
