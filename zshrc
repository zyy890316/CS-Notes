# If you come from bash you might have to change your $PATH.
ZSH_DISABLE_COMPFIX=true
export TERM="xterm-256color"
export PATH=$HOME/bin:/usr/local/bin:/.toolbox/bin:$PATH
export PATH=$HOME/.toolbox/bin:$PATH
export PATH=$PATH:$HOME/.odin-tools/env/OdinRetrievalScript-1.0/runtime/bin
export PATH=/usr/local/share/python:$PATH

export NVM_DIR="$([ -z "${XDG_CONFIG_HOME-}" ] && printf %s "${HOME}/.nvm" || printf %s "${XDG_CONFIG_HOME}/nvm")"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh" # This loads nvm

eval "$(pyenv init -)"

# Path to your oh-my-zsh installation.
export ZSH="/Users/yiyiz/.oh-my-zsh"

# Set name of the theme to load. Optionally, if you set this to "random"
# it'll load a random theme each time that oh-my-zsh is loaded.
# See https://github.com/robbyrussell/oh-my-zsh/wiki/Themes
#ZSH_THEME="robbyrussell"
ZSH_THEME="powerlevel9k/powerlevel9k"

prompt_zsh_battery_level() {
    percentage=`pmset -g batt | egrep "([0-9]+\%).*" -o --colour=auto | cut -f1 -d';' | grep -oe '\([0-9.]*\)'`
    local color='%F{red}'
    local symbol="\uf00d"
if [ "$(bc <<< "scale=2 ; $percentage<25")" = '1' ]
    then symbol="\uf244" ; color='%F{red}' ;
        #Less than 25
        fi  
if [ "$(bc <<< "scale=2 ; $percentage>=25")" = '1' ] && [ "$(bc <<< "scale=2 ; $percentage<50")" = '1' ]
    then symbol='\uf243' ; color='%F{red}' ;
    #25%
    fi
if [ "$(bc <<< "scale=2 ; $percentage>=50")" = '1' ] && [ "$(bc <<< "scale=2 ; $percentage<75")" = '1' ]  
    then symbol="\uf242" ; color='%F{yellow}' ;
     #50%
     fi
if [ "$(bc <<< "scale=2 ; $percentage>=75")" = '1' ] && [ "$(bc <<< "scale=2 ; $percentage<100")" = '1' ]
    then symbol="\uf241" ; color='%F{blue}' ;
        #75%
        fi  
if [ "$(bc <<< "scale=2 ; $percentage>99")" = '1' ]
    then symbol="\uf240" ; color='%F{green}' ;
        #100%
        fi
pmset -g batt | grep "discharging" >& /dev/null
if [ $? -eq 0 ]; then
    true;
else ;
   color='%F{green}' ;
fi
echo -n "%{$color%}$symbol " 
}

POWERLEVEL9K_MODE='nerdfont-complete'
POWERLEVEL9K_SHORTEN_DIR_LENGTH=3
POWERLEVEL9K_SHORTEN_STRATEGY="truncate_from_right"

# battery
POWERLEVEL9K_BATTERY_CHARGING='yellow'
POWERLEVEL9K_BATTERY_CHARGED='green'
POWERLEVEL9K_BATTERY_DISCONNECTED='$DEFAULT_COLOR'
POWERLEVEL9K_BATTERY_LOW_THRESHOLD='10'
POWERLEVEL9K_BATTERY_LOW_COLOR='red'
POWERLEVEL9K_BATTERY_ICON=`prompt_zsh_battery_level`

# enable the vcs segment in general
POWERLEVEL9K_SHOW_CHANGESET=true
# # just show the 6 first characters of changeset
POWERLEVEL9K_CHANGESET_HASH_LENGTH=3

POWERLEVEL9K_LEFT_PROMPT_ELEMENTS=(os_icon dir vcs)
POWERLEVEL9K_RIGHT_PROMPT_ELEMENTS=(status command_execution_time load ram_joined battery)

# Set list of themes to load
# Setting this variable when ZSH_THEME=random
# cause zsh load theme from this variable instead of
# looking in ~/.oh-my-zsh/themes/
# An empty array have no effect
# ZSH_THEME_RANDOM_CANDIDATES=( "robbyrussell" "agnoster" )

# Uncomment the following line to use case-sensitive completion.
# CASE_SENSITIVE="true"

# Uncomment the following line to use hyphen-insensitive completion. Case
# sensitive completion must be off. _ and - will be interchangeable.
# HYPHEN_INSENSITIVE="true"

# Uncomment the following line to disable bi-weekly auto-update checks.
# DISABLE_AUTO_UPDATE="true"

# Uncomment the following line to change how often to auto-update (in days).
# export UPDATE_ZSH_DAYS=13

# Uncomment the following line to disable colors in ls.
# DISABLE_LS_COLORS="true"

# Uncomment the following line to disable auto-setting terminal title.
# DISABLE_AUTO_TITLE="true"

# Uncomment the following line to enable command auto-correction.
ENABLE_CORRECTION="true"

# Uncomment the following line to display red dots whilst waiting for completion.
COMPLETION_WAITING_DOTS="true"

# Uncomment the following line if you want to disable marking untracked files
# under VCS as dirty. This makes repository status check for large repositories
# much, much faster.
# DISABLE_UNTRACKED_FILES_DIRTY="true"

# Uncomment the following line if you want to change the command execution time
# stamp shown in the history command output.
# You can set one of the optional three formats:
# "mm/dd/yyyy"|"dd.mm.yyyy"|"yyyy-mm-dd"
# or set a custom format using the strftime function format specifications,
# see 'man strftime' for details.
HIST_STAMPS="mm/dd/yyyy"

# Would you like to use another custom folder than $ZSH/custom?
# ZSH_CUSTOM=/path/to/new-custom-folder

# Which plugins would you like to load? (plugins can be found in ~/.oh-my-zsh/plugins/*)
# Custom plugins may be added to ~/.oh-my-zsh/custom/plugins/
# Example format: plugins=(rails git textmate ruby lighthouse)
# Add wisely, as too many plugins slow down shell startup.
plugins=(git git-prompt autojump history zsh-syntax-highlighting zsh-autosuggestions z history-substring-search)

#source /Users/yiyiz/.zsh/zsh-git-prompt-master/zshrc.sh
source $ZSH/oh-my-zsh.sh
source ~/.fonts/*.sh

# User configuration

# export MANPATH="/usr/local/man:$MANPATH"

# You may need to manually set your language environment
# export LANG=en_US.UTF-8

# Preferred editor for local and remote sessions
# if [[ -n $SSH_CONNECTION ]]; then
#   export EDITOR='vim'
# else
#   export EDITOR='mvim'
# fi

# Compilation flags
# export ARCHFLAGS="-arch x86_64"

# ssh
# export SSH_KEY_PATH="~/.ssh/rsa_id"

# Set personal aliases, overriding those provided by oh-my-zsh libs,
# plugins, and themes. Aliases can be placed here, though oh-my-zsh
# users are encouraged to define aliases within the ZSH_CUSTOM folder.
# For a full list of active aliases, run `alias`.
#
# Example aliases
# alias zshconfig="mate ~/.zshrc"
# alias ohmyzsh="mate ~/.oh-my-zsh"
alias sdc='mwinit -o && ada credentials update --account=963828652515 --provider=conduit --role=CardClient'

######################### zsh options ################################                   
setopt ALWAYS_TO_END           # Push that cursor on completions.                        
setopt AUTO_NAME_DIRS          # change directories  to variable names                   
setopt AUTO_PUSHD              # push directories on every cd                            
setopt NO_BEEP                 # self explanatory                                        

######################### history options ############################                   
setopt EXTENDED_HISTORY        # store time in history                                   
setopt HIST_EXPIRE_DUPS_FIRST  # unique events are more usefull to me                    
setopt HIST_VERIFY             # Make those history commands nice                        
setopt INC_APPEND_HISTORY      # immediatly insert history into history file             
HISTSIZE=16000                 # spots for duplicates/uniques                            
SAVEHIST=15000                 # unique events guaranteed                                
HISTFILE=~/.history

# Tell the terminal about the working directory when pwd changes
if [[ $TERM_PROGRAM == "Apple_Terminal" ]] && [[ -z "$INSIDE_EMACS" ]] {
  function chpwd {
    # Replace spaces with %20
    # If paths contain other exotic characters, better escaping is needed
    printf '\e]7;%s\a' "file://$HOSTNAME${PWD// /%20}"
  }
  chpwd
}

#export PS1="%(5~|%-1~/…/%3~|%4~) $ " 


 if [[ $TERM == "xterm-256color" ]]; then
   #Mac OSX/BSD bindings for Home/End/Del
   bindkey "^[[H" beginning-of-line
   bindkey "^[[F" end-of-line
   bindkey "^[[3~" delete-char
 fi
 

if which rbenv > /dev/null; then eval "$(rbenv init -)"; fi

alias vi='vim'
# https://w.amazon.com/index.php/OdinOnMac
alias odin="ssh -L 2009:localhost:2009 dev-dsk-yiyiz-2c-i-75dea6af.us-west-2.amazon.com -f -N"
export PATH=/usr/local/opt/curl-openssl/bin:$PATH
export JAVA_HOME=/Library/Java/JavaVirtualMachines/amazon-corretto-8.jdk/Contents/Home

export PATH=$HOME/.toolbox/bin:$PATH
[ -s "$NVM_DIR/bash_completion" ] && \. "$NVM_DIR/bash_completion"  # This loads nvm bash_completion

export PATH=/Users/yiyiz/aws-cli/bin:$PATH
export PATH=/usr/local/opt/curl/bin:$PATH
export PATH=/Users/yiyiz/aws-cli/bin:$PATH
export PATH=/usr/local/opt/curl/bin:$PATH

source $HOME/.cardcli_profile
