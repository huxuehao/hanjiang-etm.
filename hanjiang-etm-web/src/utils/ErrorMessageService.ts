import { ElMessage } from "element-plus";

interface ErrorMessage {
    code: number;
    message: string;
}

class ErrorMessageService {
    private static instance: ErrorMessageService
    private message: string | null = null;

    // 构造器私有化，防止外部构造
    private constructor() {}

    /**
     * 实例化方法
     */
    public static getInstance(): ErrorMessageService {
        if(!ErrorMessageService.instance) {
            ErrorMessageService.instance = new ErrorMessageService()
        }
        return ErrorMessageService.instance
    } 

    /**
     * 发送错误提示
     * @param msg 提示内容
     */
    public send(msg: string, type:string = "error"): void {
        if(this.message) {
            return;
        }
        this.message = msg

        ElMessage({
            message: msg,
            type: type,
            plain: true,
            duration: 3000
        })

        setTimeout(() => {
            this.clearMsg()
        }, 3000)
    }

    /**
     * 清理消息
     */
    public clearMsg():void {
        this.message = null;
    }
}

export const ErrorMessage = ErrorMessageService.getInstance();